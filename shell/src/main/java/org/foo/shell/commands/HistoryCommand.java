package org.foo.shell.commands;

import org.foo.shell.History;

import java.io.PrintStream;
import java.util.List;

public class HistoryCommand extends BasicCommand {
    private final History history;

    public HistoryCommand(History history) {
        this.history = history;
    }

    @Override
    public void exec(String args, PrintStream out, PrintStream err) throws Exception {
        List<String> historyList = history.get();
        int count = Math.min(
                args != null ? Integer.parseInt(args.trim()) : historyList.size(),
                historyList.size()
        );

        for (int i = count; i > 0; i--) {
            out.println(historyList.remove(historyList.size() - 1));
        }
    }
}
