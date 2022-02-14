 ArrayList<String> list = new ArrayList<>();
        list.add("zs");
        list.add("ls");
        list.add("wu");

class ArrayList{
    
    Object[] elementData; // 就是ArrayList底层所维护的数组
    Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
    int size;// 存储了多少个元素
    int DEFAULT_CAPACITY = 10;// 默认初始长度
    int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;// 最多允许存储多少个数据
    
    public ArrayList() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }
    
   public boolean add(E e) {
       //                       0 + 1                     
        ensureCapacityInternal(size + 1);  
       
       // elementData: 长度为10 的空数组
       
        elementData[size++] = e;
        return true;
    }

    //                                         1
   private void ensureCapacityInternal(int minCapacity) {
       
       // 构造方法刚赋过值: 结果为真
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            //   10                       10           1
            minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
        }

       //                     10
        ensureExplicitCapacity(minCapacity);
    }
    
    //                                            10
     private void ensureExplicitCapacity(int minCapacity) {
        modCount++;

        //  10     -      0 > 0    
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);// 扩容方法
    }
    
    //                       10
     private void grow(int minCapacity) {
        // oldCapacity = 0
        int oldCapacity = elementData.length;
         // newCapacity = 1.5倍oldCapacity = 0
        int newCapacity = oldCapacity + (oldCapacity >> 1);
         
         //  0   - 10  < 0
        if (newCapacity - minCapacity < 0)
            // newCapacity = 10
            newCapacity = minCapacity;
         
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
       
         // Arrays.copyOf 从旧数组复制出一个新数组
         // 新数组的长度: newCapacity = 10
         //  elementData: 长度为10的空数组
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

   
}
  
  
  
//        1,  HashMap是Map接口一个具体实现
//        2,  HashMap的底层结构是 数组 + 链表 + 红黑树 (红黑树是jdk1.8版本新加的结构)
//        3,  数组的默认初始长度,   数组的扩容机制
//        4,  HashMap存储key是无序的
//        5,  HashMap不允许存储重复的key值 , 对于HashMap什么key的重复的定义是什么?
//        6,  HashMap允许存户 null 键
//        7,  HashMap线程不安全
  
1, 是Map接口一个子实现
2,  Hashtable是jdk1.0时候产生,  Map接口是jdk1.2的时候产生
3, 底层结构 : 数组 + 链表, (和jdk1.8之前的HashMap一样)
4, 默认的初始容量11,   扩容机制: 扩为原本的2倍+1
5, 无序, 不允许重复key
6, 不允许存储null 键, 也不允许存储null值
7, 线程安全

Hashtable计算hash值和计算下标的方式也不同于HashMap


        HashMap<String, String> map = new HashMap<>();
        map.put("zs", "18");
        map.put("ls", "19");
        map.put("wu", "20");
        map.put("zl", "21");
  
  class HashMap{
    
    Node<K,V>[] table;// hashmap的底层数组
    
    float loadFactor;// 加载因子
    float DEFAULT_LOAD_FACTOR = 0.75f;
     int threshold;// 阈值 = 加载因子 * 底层数组长度
    
    public HashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;  
    }
    
    // 把一个key计算成一个int值的hash方法, 计算的结果就是key的hash值
     static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
    public V put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
    }
    
    final V putVal(int hash, K key, V value, boolean onlyIfAbsent,  boolean evict) {
        
        
        Node<K,V>[] tab; 
        Node<K,V> p; 
        int n, i;
        // table == null
        // tab == null
        if ((tab = table) == null || (n = tab.length) == 0)
            // resize() : hashmap的扩容方法
            
            n = (tab = resize()).length;
        if ((p = tab[i = (n - 1) & hash]) == null)
            tab[i] = newNode(hash, key, value, null);
        else {
            Node<K,V> e; K k;
            if (p.hash == hash &&
                ((k = p.key) == key || (key != null && key.equals(k))))
                e = p;
            else if (p instanceof TreeNode)
                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
            else {
                for (int binCount = 0; ; ++binCount) {
                    if ((e = p.next) == null) {
                        p.next = newNode(hash, key, value, null);
                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                            treeifyBin(tab, hash);
                        break;
                    }
                    if (e.hash == hash &&
                        ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e;
                }
            }
            if (e != null) { // existing mapping for key
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
                afterNodeAccess(e);
                return oldValue;
            }
        }
        ++modCount;
        if (++size > threshold)
            resize();
        afterNodeInsertion(evict);
        return null;
    }
    
    int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    
    final Node<K,V>[] resize() {
        // oldTab == null
        Node<K,V>[] oldTab = table;
        
        // oldCap = 0
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        // oldThr = threshold = 0 
        int oldThr = threshold;
        
        
        int newCap, newThr = 0;
        
        
        if (oldCap > 0) {
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            }
            else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                     oldCap >= DEFAULT_INITIAL_CAPACITY)
                newThr = oldThr << 1; // double threshold
        }
        else if (oldThr > 0) // initial capacity was placed in threshold
            newCap = oldThr;
        else {               // zero initial threshold signifies using defaults
            
            // newCap = DEFAULT_INITIAL_CAPACITY = 16
            newCap = DEFAULT_INITIAL_CAPACITY;
            // newThr =  0.75 * 16  = 12
            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        if (newThr == 0) {
            float ft = (float)newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                      (int)ft : Integer.MAX_VALUE);
        }
        // threshold = 12
        threshold = newThr;
        @SuppressWarnings({"rawtypes","unchecked"})
        // 创建一个长度为16 的数组newTab
            Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
        // table 长度为16 的数组
        table = newTab;
        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {
                Node<K,V> e;
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    if (e.next == null)
                        newTab[e.hash & (newCap - 1)] = e;
                    else if (e instanceof TreeNode)
                        ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                    else { // preserve order
                        Node<K,V> loHead = null, loTail = null;
                        Node<K,V> hiHead = null, hiTail = null;
                        Node<K,V> next;
                        do {
                            next = e.next;
                            if ((e.hash & oldCap) == 0) {
                                if (loTail == null)
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;
                            }
                            else {
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        return newTab;
    }
    
}
  
  //        HashMap()
//        构造一个具有默认初始容量 (16) 和默认加载因子 (0.75) 的空 HashMap。
//        HashMap(int initialCapacity)
//        构造一个带指定初始容量和默认加载因子 (0.75) 的空 HashMap。
        HashMap<String, String> map = new HashMap<>(10);

  
  class HashMap{
    
   public HashMap(int initialCapacity) {
       //          10            0.75f
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }
    
    public HashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " +
                                               initialCapacity);
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " +
                                               loadFactor);
        
        this.loadFactor = loadFactor;
        //      initialCapacity = 10       
        //      tableSizeFor(initialCapacity) = 16
        this.threshold = tableSizeFor(initialCapacity);
    }
    
    static final int tableSizeFor(int cap) {
        // 10 -1
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
       //  n = 15
       
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }
    
    
    // 
    final Node<K,V>[] resize() {
        // oldTab = null
        Node<K,V>[] oldTab = table;
        // oldCap = 0
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        
        // oldThr = 16
        int oldThr = threshold;
        
        int newCap, newThr = 0;
        
        
        if (oldCap > 0) {
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            }
            else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                     oldCap >= DEFAULT_INITIAL_CAPACITY)
                newThr = oldThr << 1; // double threshold
        }
        else if (oldThr > 0) // initial capacity was placed in threshold
            
            // newCap = 16
            newCap = oldThr;
        else {               // zero initial threshold signifies using defaults
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        
        
        if (newThr == 0) {
            
            // ft = 16 * 0.75 = 12
            float ft = (float)newCap * loadFactor;
            // newThr = 12
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                      (int)ft : Integer.MAX_VALUE);
        }
        
        // threshold = 12
        threshold = newThr;
        @SuppressWarnings({"rawtypes","unchecked"})
        // 创建一个长度为16的数组
            Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
        table = newTab;
        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {
                Node<K,V> e;
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    if (e.next == null)
                        newTab[e.hash & (newCap - 1)] = e;
                    else if (e instanceof TreeNode)
                        ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                    else { // preserve order
                        Node<K,V> loHead = null, loTail = null;
                        Node<K,V> hiHead = null, hiTail = null;
                        Node<K,V> next;
                        do {
                            next = e.next;
                            if ((e.hash & oldCap) == 0) {
                                if (loTail == null)
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;
                            }
                            else {
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        return newTab;
    }
    
}
  
  class HashMap{
    
    Node<K,V>[] table;// hashmap的底层数组
    
    float loadFactor;// 加载因子
    float DEFAULT_LOAD_FACTOR = 0.75f;
    int threshold;// 阈值 = 加载因子 * 底层数组长度
     public V put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
    }
    final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
        Node<K,V>[] tab; Node<K,V> p; int n, i;
        if ((tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;
        if ((p = tab[i = (n - 1) & hash]) == null)
            tab[i] = newNode(hash, key, value, null);
        else {
            Node<K,V> e; K k;
            if (p.hash == hash &&
                ((k = p.key) == key || (key != null && key.equals(k))))
                e = p;
            else if (p instanceof TreeNode)
                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
            else {
                for (int binCount = 0; ; ++binCount) {
                    if ((e = p.next) == null) {
                        p.next = newNode(hash, key, value, null);
                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                            treeifyBin(tab, hash);
                        break;
                    }
                    if (e.hash == hash &&
                        ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e;
                }
            }
            if (e != null) { // existing mapping for key
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
                afterNodeAccess(e);
                return oldValue;
            }
        }
        ++modCount;
        // 如果上述添加完成 size 要加一
        // 如果存储元素大于阈值, 那么 要扩容
        if (++size > threshold)
            resize();
        afterNodeInsertion(evict);
        return null;
    }
    
    // 假如第二次触发扩容方法: 存储元素是13
    //  假如全部默认的情况下
  
    final Node<K,V>[] resize() {
        // oldTab: 长度为16 的数组
        Node<K,V>[] oldTab = table;
        // oldCap = 16
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        
        // oldThr = 12
        int oldThr = threshold;
        
        
        int newCap, newThr = 0;
        
        
        if (oldCap > 0) {
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            }// newCap = 16 <<< 1 = 32
            else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                     oldCap >= DEFAULT_INITIAL_CAPACITY)
                // newThr = 12 << 1 = 24
                newThr = oldThr << 1; // double threshold
        }
        else if (oldThr > 0) // initial capacity was placed in threshold
            newCap = oldThr;
        else {               // zero initial threshold signifies using defaults
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        // newCap = 32
        // newThr = 24
        if (newThr == 0) {
            float ft = (float)newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                      (int)ft : Integer.MAX_VALUE);
        }
        // threshold = 24
        threshold = newThr;
        @SuppressWarnings({"rawtypes","unchecked"})
        // 创建一个长度为32的数组
            Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
        table = newTab;
        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {
                Node<K,V> e;
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    if (e.next == null)
                        newTab[e.hash & (newCap - 1)] = e;
                    else if (e instanceof TreeNode)
                        ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                    else { // preserve order
                        Node<K,V> loHead = null, loTail = null;
                        Node<K,V> hiHead = null, hiTail = null;
                        Node<K,V> next;
                        do {
                            next = e.next;
                            if ((e.hash & oldCap) == 0) {
                                if (loTail == null)
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;
                            }
                            else {
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        return newTab;
    }
    
}
  
  class HashMap{
    
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
    
    public V put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
    }
    
    final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
        Node<K,V>[] tab; Node<K,V> p; int n, i;
        
        
        if ((tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;
        
        // n 是底层数组长度,   2的幂值
        // i = (n - 1) & hash 
        // hash和数组长度取模的下标 ----> i
        // p 就是 取模之后数组的对应下标位置
        if ((p = tab[i = (n - 1) & hash]) == null)
            // 如果这个要散列的下标位置, 从来没有存储过元素, 直接存储
            tab[i] = newNode(hash, key, value, null);
        else {
            // 如果这个要散列的下标位置, 已经存储过元素
            // 并且存储的元素是 p
            Node<K,V> e; K k;
            if (p.hash == hash &&
                ((k = p.key) == key || (key != null && key.equals(k))))
                // 重复, e和此时存储的key重复的结点
                e = p;
            else if (p instanceof TreeNode)
                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
            else {
                for (int binCount = 0; ; ++binCount) {
                    if ((e = p.next) == null) {
                        p.next = newNode(hash, key, value, null);
                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                            treeifyBin(tab, hash);
                        break;
                    }
                    if (e.hash == hash &&
                        ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e;
                }
            }
            
            if (e != null) { //e就是重复的结点
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue == null)
                    // 用新的value值覆盖旧的重复key的value
                    e.value = value;
                afterNodeAccess(e);
                return oldValue;
            }
        }
        ++modCount;
        if (++size > threshold)
            resize();
        afterNodeInsertion(evict);
        return null;
    }
    
   Node<K,V> newNode(int hash, K key, V value, Node<K,V> next) {
        return new Node<>(hash, key, value, next);
    }
    
     static class Node<K,V> implements Map.Entry<K,V> {
        final int hash;
        final K key;
        V value;
        Node<K,V> next;
     }
}

