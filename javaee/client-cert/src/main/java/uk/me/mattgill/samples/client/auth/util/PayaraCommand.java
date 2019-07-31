package uk.me.mattgill.samples.client.auth.util;

import static java.util.logging.Level.WARNING;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

import javax.security.auth.Subject;

import com.sun.enterprise.v3.common.PlainTextActionReporter;

import org.glassfish.api.ActionReport;
import org.glassfish.api.admin.AdminCommand;
import org.glassfish.api.admin.CommandRunner;
import org.glassfish.api.admin.CommandRunner.CommandInvocation;
import org.glassfish.internal.api.Globals;
import org.glassfish.security.common.Group;
import org.glassfish.security.common.PrincipalImpl;

import uk.me.mattgill.samples.client.auth.DomainConfiguration;

public class PayaraCommand {

    private static final Logger LOGGER = Logger.getLogger(DomainConfiguration.class.getName());

    private final CommandInvocation invocation;

    private final AdminCommand delegate;

    private final ActionReport report;

    public PayaraCommand(String commandName, String... arguments) {
        CommandRunner runner = Globals.getDefaultHabitat().getService(CommandRunner.class);
        this.report = new PlainTextActionReporter();
        this.delegate = runner.getCommand(commandName, report, LOGGER);
        Subject subject = new Subject();
        subject.getPrincipals().add(new PrincipalImpl("admin"));
        subject.getPrincipals().add(new Group("asadmin"));
        this.invocation = runner.getCommandInvocation(commandName, report, subject);
    }

    public PayaraCommand execute() {
        invocation.execute(delegate);
        return this;
    }

    public boolean isSuccess() {
        return !report.hasFailures();
    }

    public String getOutput() {
        try (OutputStream outputStream = new ByteArrayOutputStream()) {
            report.writeReport(outputStream);
            return outputStream.toString();
        } catch (IOException ex) {
            LOGGER.log(WARNING, "Unable to get result from command.", ex);
        }
        return null;
    }
}