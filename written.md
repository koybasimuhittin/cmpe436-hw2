# Q1(g) - 2021400162

## 1. Why Lamport cannot detect concurrency

My pair from (e) is P2.3 and P3.5: L(P2.3) = 4 < L(P3.5) = 6, but they are concurrent. Slide 19 only gives one direction (s -> t implies L(s) < L(t), "the converse may not be true"), and P3.5 is large only because P3 had more events, not because it knows anything about P2. One number cannot show which process its value came from, so a smaller L does not mean happened-before. The vector clock keeps one entry per process: V(P2.3) = [2,4,1] and V(P3.5) = [4,0,6]. V(P3.5)[2] = 0 < 4 shows P3.5 never heard of P2.3, and V(P2.3)[3] = 1 < 6 shows P3.5 is not before P2.3, so P2.3 || P3.5.

## 2. "Concurrent with" is not transitive

Take a = P3.0, b = P2.1, c = P1.1, with V(a) = [0,0,1], V(b) = [0,2,0], V(c) = [2,0,1].

- a || b: a is bigger in entry 3 (1 > 0), b is bigger in entry 2 (2 > 0), so neither vector is <= the other.
- b || c: b is bigger in entry 2 (2 > 0), c is bigger in entry 1 (2 > 0), so again not comparable.
- a -> c: [0,0,1] <= [2,0,1] and they are not equal, so V(a) < V(c). In the trace, m1 is sent from P3.0 and received at P1.1.

So a || b and b || c, but a -> c, which means || is not transitive.
