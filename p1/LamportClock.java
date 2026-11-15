public class LamportClock {
    int c;

    public LamportClock() {
        c = 1;
    }

    // internal event
    public void tick() {
        c = c + 1;
    }

    // send event
    public void sendAction() {
        // include c in message
        c = c + 1;
    }

    // receive event
    public void receiveAction(int src, int sentValue) {
        c = Util.max(c, sentValue) + 1;
    }
}
