public class VectorClock {
    public int[] v;
    int myId;
    int N;

    public VectorClock(int numProc, int id) {
        myId = id;
        N = numProc;
        v = new int[numProc];
        for (int i = 0; i < N; i++)
            v[i] = 0;
        v[myId] = 1;
    }

    // internal event
    public void tick() {
        v[myId]++;
    }

    // send event
    public void sendAction() {
        // include the vector in the message
        v[myId]++;
    }

    // receive event
    public void receiveAction(int[] sentValue) {
        // take component-wise maximum of vectors
        for (int i = 0; i < N; i++)
            v[i] = Util.max(v[i], sentValue[i]);
        v[myId]++;
    }
}
