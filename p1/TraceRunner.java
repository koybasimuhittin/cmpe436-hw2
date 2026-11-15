import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TraceRunner {

    static class Msg {
        int sender;
        int l;
        int[] v;
        int d;
    }

    static class State {
        int p, k;
        int l;
        int[] v;
        int[] d;

        String name() {
            return "P" + p + "." + k;
        }
    }

    static int n;
    static LamportClock[] lc;
    static VectorClock[] vc;
    static DirectClock[] dc;
    static List<List<State>> states = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("usage: java TraceRunner <trace-file> <query-file>");
            return;
        }
        runTrace(readLines(args[0]));
        printStates();
    }

    static List<String> readLines(String file) throws IOException {
        List<String> out = new ArrayList<>();
        for (String line : Files.readAllLines(Paths.get(file))) {
            int hash = line.indexOf('#');
            if (hash >= 0)
                line = line.substring(0, hash);
            line = line.trim();
            if (!line.isEmpty())
                out.add(line);
        }
        return out;
    }

    static void runTrace(List<String> lines) {
        Map<String, Msg> inTransit = new HashMap<>();
        for (String line : lines) {
            String[] w = line.split("\\s+");
            if (w[0].equals("processes")) {
                n = Integer.parseInt(w[1]);
                lc = new LamportClock[n];
                vc = new VectorClock[n];
                dc = new DirectClock[n];
                for (int i = 0; i < n; i++) {
                    lc[i] = new LamportClock();
                    vc[i] = new VectorClock(n, i);
                    dc[i] = new DirectClock(n, i);
                    states.add(new ArrayList<>());
                    save(i);
                }
                continue;
            }

            int i = Integer.parseInt(w[0]) - 1;
            if (w[1].equals("internal")) {
                lc[i].tick();
                vc[i].tick();
                dc[i].tick();
            } else if (w[1].equals("send")) {
                Msg msg = new Msg();
                msg.sender = i;
                msg.l = lc[i].c;
                msg.v = vc[i].v.clone();
                msg.d = dc[i].clock[i];
                inTransit.put(w[2], msg);
                lc[i].sendAction();
                vc[i].sendAction();
                dc[i].sendAction();
            } else if (w[1].equals("receive")) {
                Msg msg = inTransit.remove(w[2]);
                lc[i].receiveAction(msg.sender, msg.l);
                vc[i].receiveAction(msg.v);
                dc[i].receiveAction(msg.sender, msg.d);
            } else {
                System.err.println("unknown line: " + line);
                continue;
            }
            save(i);
        }
    }

    static void save(int i) {
        State s = new State();
        s.p = i + 1;
        s.k = states.get(i).size();
        s.l = lc[i].c;
        s.v = vc[i].v.clone();
        s.d = dc[i].clock.clone();
        states.get(i).add(s);
    }

    static void printStates() {
        for (List<State> list : states)
            for (State s : list)
                System.out.println(s.name() + " L=" + s.l + " V=" + vec(s.v) + " D=" + vec(s.d));
    }

    static String vec(int[] a) {
        return Arrays.toString(a).replace(" ", "");
    }
}
