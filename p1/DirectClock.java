public class DirectClock {
    public int[] clock;
    int myId;

    public DirectClock(int numProc, int id) {
        myId = id;
        clock = new int[numProc];
        for (int i = 0; i < numProc; i++)
            clock[i] = 0;
        clock[myId] = 1;
    }

    // internal event
    public void tick() {
        clock[myId]++;
    }

    // send event
    public void sendAction() {
        // sentValue = clock[myId];
        clock[myId]++;
    }

    // receive event
    public void receiveAction(int sender, int sentValue) {
        clock[sender] = Util.max(clock[sender], sentValue);
        clock[myId] = Util.max(clock[myId], sentValue) + 1;
    }
}
