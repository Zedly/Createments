package zedly.createments;

/**
 * Replaces functionality of apache commons because I'm not dealing with tat's code
 * @author Dennis
 */
public class ArrayUtils {

    public static boolean contains(Object[] array, Object element) {
        return indexOf(array, element) != -1;
    }

    public static int indexOf(Object[] array, Object element) {
        for (int i = 0; i < array.length; i++) {
            Object o = array[i];
            if (o == element) {
                return i;
            }
        }
        return -1;
    }

}
