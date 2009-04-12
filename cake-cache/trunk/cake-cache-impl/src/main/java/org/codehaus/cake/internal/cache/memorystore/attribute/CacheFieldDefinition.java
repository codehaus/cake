package org.codehaus.cake.internal.cache.memorystore.attribute;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.internal.codegen.attribute.FieldDefinition;
import org.codehaus.cake.util.attribute.Attribute;

public class CacheFieldDefinition<T> extends FieldDefinition<CacheFieldDefinition<T>> {

    private AccessAction accessAction = AccessAction.NOTHING;
    private CreateAction createAction = CreateAction.DEFAULT;
    private Object createSetValue;
    private ModifyAction modifyAction = ModifyAction.KEEP_EXISTING;

    private boolean readMapOnCreate = false;

    private boolean readMapOnModify = false;

    public AccessAction getAccessAction() {
        return accessAction;
    }

    public CreateAction getCreateAction() {
        return createAction;
    }

    public Object getCreateSetValue() {
        return createSetValue;
    }

    public ModifyAction getModifyAction() {
        return modifyAction;
    }

    public CacheFieldDefinition(Attribute<T> attribute) {
        super(attribute.getType(), attribute.getName());
        setAttribute(attribute);
        setFinal(true);
    }

    public boolean isReadMapOnCreate() {
        return readMapOnCreate;
    }

    public boolean isReadMapOnModify() {
        return readMapOnModify;
    }

    public CacheFieldDefinition<T> setAccessAction(AccessAction accessAction) {
        this.accessAction = accessAction;
        return this;
    }

    public CacheFieldDefinition<T> setCreateAction(CreateAction createAction) {
        this.createAction = createAction;
        return this;
    }

    public CacheFieldDefinition<T> setModifyAction(ModifyAction modifyAction) {
        this.modifyAction = modifyAction;
        return this;
    }

    public CacheFieldDefinition<T> setReadMapOnCreate(boolean readMapOnCreate) {
        this.readMapOnCreate = readMapOnCreate;
        return this;
    }

    public CacheFieldDefinition<T> setReadMapOnModify(boolean readMapOnModify) {
        this.readMapOnModify = readMapOnModify;
        return this;
    }

    public static <T> CacheFieldDefinition<T> getPredefinedConfiguration(Attribute<T> a) {
        return getPredefinedConfiguration(a, true);
    }

    private static <T> CacheFieldDefinition<T> getPredefinedConfiguration(Attribute<T> a, boolean isPublic) {
        final CacheFieldDefinition<T> sac = new CacheFieldDefinition<T>(a);
        if (a == CacheEntry.TIME_CREATED) {
            sac.setReadMapOnCreate(true).setCreateAction(CreateAction.TIMESTAMP);
        } else if (a == CacheEntry.TIME_MODIFIED) {
            sac.setReadMapOnCreate(true).setCreateAction(CreateAction.TIMESTAMP);
            sac.setReadMapOnModify(true).setModifyAction(ModifyAction.TIMESTAMP);
        } else if (a == CacheEntry.TIME_ACCESSED) {
            sac.setFinal(false);
            sac.setReadMapOnCreate(true).setCreateAction(CreateAction.TIMESTAMP);
            sac.setReadMapOnModify(true).setModifyAction(ModifyAction.TIMESTAMP);
            sac.setAccessAction(AccessAction.TIMESTAMP);
        } else if (a == CacheEntry.HITS) {
            sac.setFinal(false);
            ((CacheFieldDefinition<Long>) sac).setReadMapOnCreate(true).setCreateAction(CreateAction.SET_VALUE)
                    .setInitialValue(0L);
            sac.setReadMapOnModify(true).setModifyAction(ModifyAction.KEEP_EXISTING);
            sac.setAccessAction(AccessAction.INCREMENT);
        } else if (a == CacheEntry.VERSION) {
            ((CacheFieldDefinition<Long>) sac).setReadMapOnCreate(true).setCreateAction(CreateAction.SET_VALUE)
                    .setInitialValue(1L);
            sac.setReadMapOnModify(true).setModifyAction(ModifyAction.INCREMENT);
        } else {
            sac.setReadMapOnCreate(true).setCreateAction(CreateAction.DEFAULT);
            sac.setReadMapOnModify(true).setModifyAction(ModifyAction.DEFAULT);
        }
        return sac;
    }

    public static <T> CacheFieldDefinition<T> getPredefinedConfigurationSoft(Attribute<T> a) {
        return getPredefinedConfiguration(a, false);
    }

    public static enum AccessAction {
        INCREMENT, NOTHING, TIMESTAMP
    }

    public static enum CreateAction {
        DEFAULT, SET_VALUE, TIMESTAMP
    }

    public static enum ModifyAction {
        DEFAULT, INCREMENT, KEEP_EXISTING, TIMESTAMP;
    }
}
