/*
 * MIT License
 *
 * Copyright (c) 2020 Pasqual K. and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.derklaro.privateservers.reformcloud.v2.cloud;

import com.github.derklaro.privateservers.api.cloud.CloudServiceManager;
import com.github.derklaro.privateservers.api.cloud.configuration.CloudServiceConfiguration;
import com.github.derklaro.privateservers.api.cloud.util.CloudService;
import com.github.derklaro.privateservers.common.cloud.DefaultCloudServiceManager;
import com.github.derklaro.privateservers.common.util.EmptyArrayList;
import org.jetbrains.annotations.NotNull;
import systems.refomcloud.reformcloud2.embedded.Embedded;
import systems.reformcloud.reformcloud2.executor.api.ExecutorAPI;
import systems.reformcloud.reformcloud2.executor.api.configuration.gson.JsonConfiguration;
import systems.reformcloud.reformcloud2.executor.api.groups.ProcessGroup;
import systems.reformcloud.reformcloud2.executor.api.groups.template.RuntimeConfiguration;
import systems.reformcloud.reformcloud2.executor.api.groups.template.Template;
import systems.reformcloud.reformcloud2.executor.api.groups.template.Version;
import systems.reformcloud.reformcloud2.executor.api.process.ProcessState;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

class ReformCloudV2CloudServiceManager extends DefaultCloudServiceManager {

    static final CloudServiceManager INSTANCE = new ReformCloudV2CloudServiceManager();

    @Override
    public @NotNull CompletableFuture<CloudService> createCloudService(@NotNull String group, @NotNull String templateName,
                                                                       @NotNull String templateBackend, @NotNull CloudServiceConfiguration cloudServiceConfiguration) {
        Optional<ProcessGroup> processGroup = ExecutorAPI.getInstance().getProcessGroupProvider().getProcessGroup(group);
        if (!processGroup.isPresent()) {
            return CompletableFuture.completedFuture(null);
        }

        return ExecutorAPI.getInstance().getProcessProvider().createProcess().group(processGroup.get())
                .extra(new JsonConfiguration().add("cloudServiceConfiguration", cloudServiceConfiguration))
                .template(this.getOrCreateTemplate(templateName, templateBackend, processGroup.get()))
                .prepare()
                .thenApplyAsync(processWrapper -> {
                    processWrapper.setRuntimeStateAsync(ProcessState.STARTED);
                    return ReformCloudV2CloudService.fromProcessInformation(processWrapper.getProcessInformation())
                            .orElseThrow(() -> new RuntimeException("Unable to create CloudService from ProcessWrapper"));
                });
    }

    @NotNull
    private Template getOrCreateTemplate(@NotNull String name, @NotNull String backend, @NotNull ProcessGroup processGroup) {
        Template template = processGroup.getTemplate(name);
        if (template != null) {
            return template;
        }

        template = new Template(
                0,
                name,
                false,
                backend,
                "#",
                new RuntimeConfiguration(
                        512,
                        EmptyArrayList.emptyList(),
                        new HashMap<>()
                ), Version.PAPER_1_8_8
        );
        processGroup.getTemplates().add(template);
        ExecutorAPI.getInstance().getProcessGroupProvider().updateProcessGroup(processGroup);
        return template;
    }

    @Override
    public @NotNull Collection<CloudService> getAllCurrentlyRunningPrivateServersFromCloudSystem() {
        return ExecutorAPI.getInstance().getProcessProvider().getProcesses()
                .stream()
                .filter(e -> e.getProcessDetail().getProcessState().isStartedOrOnline())
                .map(e -> ReformCloudV2CloudService.fromProcessInformation(e).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public @NotNull UUID getCurrentServiceUniqueID() {
        return Embedded.getInstance().getCurrentProcessInformation().getProcessDetail().getProcessUniqueID();
    }
}
