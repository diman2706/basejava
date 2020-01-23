import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    int i = 0;

    void clear() {
        Arrays.fill(storage, null);
        i = 0;
    }

    void save(Resume r) {
        if (storage[i] == null){
            storage[i] = r;
            i++;
        }else storage[size()] =r;

    }

    Resume get(String uuid) {
        for (Resume resume : storage) {
            resume.uuid = uuid;
            return resume;
        }
        return null;
    }

    void delete(String uuid) {
        int index = 0;
        for (int j = 0; j <size() ; j++) {
           if (storage[j].uuid.equals(uuid)){
               index = j;
               Resume[] resultStorage = new Resume[storage.length-1];
               System.arraycopy(storage,0,resultStorage,0,index);
               System.arraycopy(storage,index+1,resultStorage,index,storage.length-index-1);
               storage = resultStorage;
               i = index;
           }
        }

    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return Arrays.copyOf(storage,size());
    }

    int size() {
        int v = 0;
        for (int j = 0; j <storage.length ; j++) {
            if (!(storage[j] == null)) v++;
        }
        return v;
    }
}
