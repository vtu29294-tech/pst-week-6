class MyQueue {
    Stack<Integer> s1 = new Stack<>();
    Stack<Integer> s2 = new Stack<>();

    public void push(int x) {
        s1.push(x);
    }

    public int pop() {
        move();
        return s2.pop();
    }

    public int peek() {
        move();
        return s2.peek();
    }

    public boolean empty() {
        return s1.empty() && s2.empty();
    }

    private void move() {
        if (s2.empty()) {
            while (!s1.empty()) {
                s2.push(s1.pop());
            }
        }
    }
}
