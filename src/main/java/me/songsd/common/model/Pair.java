package me.songsd.common.model;

/**
 * Created by SongSD on 2017/7/26.
 */
public class Pair<L, R> {

    private L left;
    private R right;

    private Pair(L left, R right) {
        setLeft(left);
        setRight(right);
    }

    public static <A, B> Pair<A, B> of(A a, B b) {
        return new Pair<A, B>(a, b);
    }

    public L getLeft() {
        return left;
    }

    public void setLeft(L left) {
        this.left = left;
    }

    public R getRight() {
        return right;
    }

    public void setRight(R right) {
        this.right = right;
    }

    public boolean equals(Pair<L, R> other) {
        return objectEquals(other.getLeft(), this.getLeft()) &&
                objectEquals(other.getRight(), this.getRight());
    }

    private boolean objectEquals(Object obj1, Object obj2) {
        return obj1 == obj2 || (obj1 != null && obj2 != null && obj1.equals(obj2));
    }
}
