package org.codehaus.cake.internal.cache.memorystore;

import org.codehaus.cake.util.ops.Ops.Op;

public class KeyValueDefinition<K, V> {
    Class<K> keyType;
    boolean useStringInterning; //saa meget sparer man jo heller ikke

    
    //equivalence, identity, man kunne ogsaa forstille sig en caseindenpendent 
    //saaleades at get("aaaa") == get("aAaA") == get("AAAA")
    boolean useIdentity;
    
    boolean storeHash;//store spreaded hash, store hash
    
    boolean spreadHash=true;
    
    
    
    
////////////VALUES
    Class<K> valueType;
    //key reference type
    //value reference type;
    Op valuePack; //only if value type is non primitive
    Op valueUnpack;///only if value type is non primitive
    boolean valueEqualsKey; //for use in sets
    //Equvalence=equals
}
