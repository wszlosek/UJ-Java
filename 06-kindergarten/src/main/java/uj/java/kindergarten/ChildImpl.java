package uj.java.kindergarten;

public final class ChildImpl extends Child {

    private Fork firstFork;
    private Fork secondFork;
    private ChildImpl leftChild;
    private ChildImpl rightChild;

    public ChildImpl(String name, int hungerSpeedMs) {
        super(name, hungerSpeedMs);
    }

    public void initForks(Fork firstFork, Fork secondFork) {
        this.firstFork = firstFork;
        this.secondFork = secondFork;
    }

    public void initNeighbour(ChildImpl leftChild, ChildImpl rightChild) {
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    public void doAction() {
        while (true) {
            if (shouldEat()) {
                synchronized (firstFork) {
                    synchronized (secondFork) {
                        super.eat();
                    }
                }
            }
        }
    }

    private boolean shouldEat() {
        return happiness() < leftChild.happiness() && happiness() < rightChild.happiness();
    }
}
