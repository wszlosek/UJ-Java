package uj.java.w3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListMerger {

    public static List<Object> mergeLists(List<?> l1, List<?> l2) {
        if (l1 == null && l2 == null) {
            return Collections.emptyList();
        } else if (l1 == null) {
            return Collections.unmodifiableList(l2);
        } else if (l2 == null) {
            return Collections.unmodifiableList(l1);
        }

        ArrayList<Object> result = new ArrayList<>();
        int smallerListSize = Math.min(l1.size(), l2.size());

        for (int i = 0; i < smallerListSize; i++) {
            result.add(l1.get(i));
            result.add(l2.get(i));
        }

        if (l1.size() > l2.size()) {
            for (int i = smallerListSize; i < l1.size(); i++) {
                result.add(l1.get(i));
            }
        } else {
            for (int i = smallerListSize; i < l2.size(); i++) {
                result.add(l2.get(i));
            }
        }

        return Collections.unmodifiableList(result);
    }
}
