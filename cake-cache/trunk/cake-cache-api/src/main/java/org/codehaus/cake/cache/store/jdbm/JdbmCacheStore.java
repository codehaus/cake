package org.codehaus.cake.cache.store.jdbm;


public class JdbmCacheStore {

    public JdbmCacheStore(JdbmCacheStoreConfiguration configuration) {
        
    }

    public static void main(String[] args) {
        // create or open fruits record manager
//        Properties props = new Properties();
//        recman = RecordManagerFactory.createRecordManager("fruits", props);
//
//        // create or load fruit basket (hashtable of fruits)
//        long recid = recman.getNamedObject("basket");
//        if (recid != 0) {
//            System.out.println("Reloading existing fruit basket...");
//            hashtable = HTree.load(recman, recid);
//            showBasket();
//        } else {
//            System.out.println("Creating new fruit basket...");
//            hashtable = HTree.createInstance(recman);
//            recman.setNamedObject("basket", hashtable.getRecid());
//        }
    }
}
