package com.coconut_palm_software.eclipsem2;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PlatformExec {
    private String command = "";
    public String stdout = "";
    public String stderr = "";
    public int exitVal = 0;
    private String cwd = null;

    public PlatformExec(String command) {
        this.command = command;
    }

    public PlatformExec(String command, String directory) {
        if (new File(directory).isDirectory()) {
            cwd = directory;
        } else {
            throw new IllegalArgumentException(directory + " is not a directory");
        }
    }

    private String getCommand() {
        String osName = System.getProperty("os.name");
        if (osName == "Windows 95") {
            return "command.com /C " + command;
        } else if (osName.startsWith("Windows")) {
            return "cmd.exe /C " + command;
        } else {
            return command;
        }
    }

    public static class Job {
        public Job(Process process, InputStream inputStream, Future<String> stdoutStream, Future<String> stderrStream) {
            this.process = process;
            this.inputStream = inputStream;
            this.stdoutStream = stdoutStream;
            this.stderrStream = stderrStream;
        }

        public final Process process;
        public final InputStream inputStream;
        public final Future<String> stdoutStream;
        public final Future<String> stderrStream;
    }

    public Job run() throws IOException {
        Runtime runtime = Runtime.getRuntime();
        final ExecutorService pool = Executors.newCachedThreadPool();

        String userDirPath = System.getProperty("user.dir");
        if (cwd != null) {
            userDirPath = cwd;
        }
        File userDir = new java.io.File(userDirPath);

        Process process = runtime.exec(getCommand(), null, userDir);
        Future<String> stdoutStream = pool.submit(new StreamGobbler(process.getInputStream()));
        Future<String> stderrStream = pool.submit(new StreamGobbler(process.getErrorStream()));

        return new Job(process, process.getInputStream(), stdoutStream, stderrStream);
    }

    private static class StreamGobbler implements Callable<String> {
        public String result = "";
        private InputStream stream;

        public StreamGobbler(InputStream stream) {
            this.stream = stream;
        }

        @Override
        public String call() throws Exception {
            InputStreamReader isr = new InputStreamReader(stream);
            BufferedReader br = new BufferedReader(isr);

            String line = "";

            do {
                try {
                    line = br.readLine();
                } catch (IOException e) {
                    StringBufferOutputStream stackTrace = new StringBufferOutputStream();
                    e.printStackTrace(new PrintStream(stackTrace));
                    result += stackTrace.toString();
                    return result;
                }
                if (line != null) {
                    result = result + line + "\n";
                }
            } while (line != null);
            return line;
        }
    }
}
