# AI_LOG - 2021400162

Tool for all prompts: Claude (Haiku 5.5), in the claude.ai chat. One chat, prompts in this order.

1. **Nov 14, Q1.** "In my distributed systems slides (Lamport clock), the code is: [constructor c = 1, sendAction with `// include c in message` then `c = c + 1`, receiveAction `c = Util.max(c, sentValue) + 1`]. Which value goes into the message, the value before or after c = c + 1? If a process at c=2 sends a message to a process at c=1, what value does the receiver end up with?"
   - Kept: the message carries the value before the increment (2), receiver gets max(1,2)+1 = 3. Same as the table in the handout.

2. **Nov 14, Q1.** "Our homework labels states, not events. [...] In my trace, P1's 2nd event is "send m2" (P1.1 -> P1.2) and P2's 2nd event is "receive m2" (P2.1 -> P2.2). There are no other messages between P1 and P2 before that. Is P1.2 -> P2.2, or are they concurrent?"
   - Kept: concurrent, because m2 is sent from P1.1, not P1.2. It said it depends on the course convention, so I checked the handout again (message carries the clock from before sendAction). This is my Q1.rel.2.

3. **Nov 14, BRAINSTORM.** "Slide 28, direct-dependency clock example: [...] Why is P3's entry for P2 still 0 even though P2's state happened before P3's state?"
   - Kept: P1 only sends its own entry, so P3 learns nothing about P2. Rejected the last part ("compute the transitive closure afterward"), that is not what the D clock is for.

4. **Nov 14, Q1.** "Can you check my Lamport clock values for this trace? [rules + my q1.trace + my values P1: 1 2 3 4 5, P2: 1 2 3 4, P3: 1 2 3 4 5 6]"
   - Rejected: it said P3.4 = max(4,5)+1 = 6 and P3.5 = 7. It used 5 for m4, but 5 is P1.4, the state after the send. m4 leaves from P1.3 with L=4, so P3.4 = max(4,4)+1 = 5. I kept my values, and my program gives the same later.

5. **Nov 15, P1.** "My Java TraceRunner gets the wrong Lamport value after a receive. For my trace I get P1.1 L=3 but by hand it is 2. My send code is: [`lc[i].sendAction();` then `msg.l = lc[i].c;`] [...] What's wrong?"
   - Kept: copy the clock into the message before calling sendAction() (commit "fix: message carries the clock from before sendAction()"). Rejected the idea to change sendAction() to return the value, because I keep the slide classes as they are.

6. **Nov 15, P1.** "In Java, if I have `int[][] M` and do `int[][] copy = M.clone();` and then do `M[0][0]++`, does `copy` change too? I want to put the matrix clock into a message object."
   - Kept: clone() of a 2D array is shallow, the rows are shared. I wrote `copy()` that clones every row.

7. **Nov 17, P1.** "How do I sort an ArrayList of my State objects in Java by an int field `l`, and if `l` is equal then by another int field `p`? Short answer please."
   - Kept the idea, but I wrote a simpler lambda `(s, t) -> s.l != t.l ? s.l - t.l : s.p - t.p` instead of the `Comparator.comparingInt(...).thenComparingInt(...)` chain.

8. **Nov 17, P1.** "Can you review this? It should return "->" [...], "==" if they are the same state, and "||" otherwise, using vector clocks. [my rel() and lessEq() code]"
   - Rejected: it said two different states can have the same vector, so I should add `Arrays.equals` for "==". In one trace two different states never have equal vectors (same process: own entry is different; different processes: it would mean each happened before the other). Also the handout says "==" only for the same state. I kept my code.
