package de.derklaro.privateservers.configuration.language.defaults;

import de.derklaro.privateservers.configuration.language.defaults.messages.Message;

public final class German extends Message {
    public German() {
        super(
                "§6§lP§6rivate§6§lS§6ervers §7§l●",
                "%prefix% §7Dein Server ist bereits gestartet.",
                "%prefix% §7Die konfigurierte Servergruppe wurde nicht gefunden, bitte kontaktieren Sie einen Administrator.",
                "%prefix% §7Der Server ist nicht getstartet",
                "%prefix% §7Der Mob wurde erfolgreich gelöscht.",
                "%prefix% §7Der Mob existiert nicht.",
                "%prefix% §7Der Mob existiert bereits.",
                "%prefix% §7Der angegebene Mob-Typ ist ungültig.",
                " %prefix% /privateservers create <MOB-TYPE> <NAME> <DISPLAY-NAME> \n %prefix% /privateservers delete <NAME> \n %prefix% /privateservers list \n %prefix% /privateservers available",
                "%prefix% §7Es ist kein privater Server gestartet.",
                "%prefix% §7Dein privater Server ist online :)",
                "%prefix% §7Dein privater Server ist offline :(",
                "%prefix% §7Du hast nicht die Berechtigung, einen Server mit dieser Vorlage zu starten :(",
                "%prefix% §7Dein Server startet, dies kann einen Moment dauern.",
                "%prefix% §7Beim Starten des Prozesses ist ein unbekannter Fehler aufgetreten.",
                " %prefix% /plugin install <name> \n %prefix% /plugin uninstall <name> \n %prefix% /plugin available \n %prefix% /plugin list \n %prefix% /plugin gui",
                "%prefix% §7Das Plugin wurde erfolgreich installiert",
                "%prefix% §7Das Plugin wurde erfolgreich deinstalliert",
                "%prefix% §7Das Plugin konnte nicht gefunden werden",
                "%prefix% §7Lösche dein Template, dies könnte einige Minuten dauern...",
                "%prefix% §7Du hast kein eigenes Template",
                "%prefix% §7Dein Template wurde gelöscht, der Server könnte trotzdem noch existieren",
                "$%prefix% §7Du hast den Vorgang abgebrochen"
        );
    }
}
