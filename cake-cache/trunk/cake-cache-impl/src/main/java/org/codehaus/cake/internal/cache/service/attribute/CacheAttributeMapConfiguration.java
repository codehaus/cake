package org.codehaus.cake.internal.cache.service.attribute;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.internal.attribute.generator.DefaultAttributeConfiguration;

public class CacheAttributeMapConfiguration extends DefaultAttributeConfiguration {

    private boolean readMapOnCreate = false;
    private boolean readMapOnModify = false;
    // private boolean isSecret=false;
    private CreateAction createAction = CreateAction.DEFAULT;
    private Object createSetValue;
    private ModifyAction modifyAction = ModifyAction.KEEP_EXISTING;
    private AccessAction accessAction = AccessAction.NOTHING;

    public CacheAttributeMapConfiguration(Attribute a, boolean allowGet, boolean allowPut, boolean isFinal,
            boolean isPrivate) {
        super(a, allowGet, allowPut, isFinal, isPrivate);
    }

    public static enum CreateAction {
        TIMESTAMP, DEFAULT, SET_VALUE
    }

    public static enum ModifyAction {
        KEEP_EXISTING, TIMESTAMP, INCREMENT, DEFAULT;
    }

    public static enum AccessAction {
        NOTHING, TIMESTAMP, INCREMENT
    }

    public boolean isReadMapOnCreate() {
        return readMapOnCreate;
    }

    public void setReadMapOnCreate(boolean readMapOnCreate) {
        this.readMapOnCreate = readMapOnCreate;
    }

    public boolean isReadMapOnModify() {
        return readMapOnModify;
    }

    public void setReadMapOnModify(boolean readMapOnModify) {
        this.readMapOnModify = readMapOnModify;
    }

    public CreateAction getCreateAction() {
        return createAction;
    }

    public void setCreateAction(CreateAction createAction) {
        this.createAction = createAction;
    }

    public Object getCreateSetValue() {
        return createSetValue;
    }

    public void setCreateSetValue(Object createSetValue) {
        this.createSetValue = createSetValue;
    }

    public ModifyAction getModifyAction() {
        return modifyAction;
    }

    public void setModifyAction(ModifyAction modifyAction) {
        this.modifyAction = modifyAction;
    }

    public AccessAction getAccessAction() {
        return accessAction;
    }

    public void setAccessAction(AccessAction accessAction) {
        this.accessAction = accessAction;
    }

    public static CacheAttributeMapConfiguration getPredefinedConfiguration(Attribute a) {
        CacheAttributeMapConfiguration sac = null;
        if (a == CacheEntry.TIME_CREATED) {
            sac = new CacheAttributeMapConfiguration(a, true, false, true, true);
            sac.setReadMapOnCreate(true);
            sac.setCreateAction(CreateAction.TIMESTAMP);
        } else if (a == CacheEntry.TIME_MODIFIED) {
            sac = new CacheAttributeMapConfiguration(a, true, false, true, true);
            sac.setReadMapOnCreate(true);
            sac.setCreateAction(CreateAction.TIMESTAMP);
            sac.setReadMapOnModify(true);
            sac.setModifyAction(ModifyAction.TIMESTAMP);
        } else if (a == CacheEntry.TIME_ACCESSED) {
            sac = new CacheAttributeMapConfiguration(a, true, false, false, false);
            sac.setReadMapOnCreate(true);
            sac.setCreateAction(CreateAction.TIMESTAMP);
            sac.setReadMapOnModify(true);
            sac.setModifyAction(ModifyAction.TIMESTAMP);
            sac.setAccessAction(AccessAction.TIMESTAMP);
        } else if (a == CacheEntry.HITS) {
            sac = new CacheAttributeMapConfiguration(a, true, false, false, false);
            sac.setReadMapOnCreate(true);
            sac.setCreateAction(CreateAction.SET_VALUE);
            sac.setCreateSetValue(0L);
            sac.setReadMapOnModify(true);
            sac.setModifyAction(ModifyAction.KEEP_EXISTING);
            sac.setAccessAction(AccessAction.INCREMENT);
        } else if (a == CacheEntry.VERSION) {
            sac = new CacheAttributeMapConfiguration(a, true, false, true, true);
            sac.setReadMapOnCreate(true);
            sac.setCreateAction(CreateAction.SET_VALUE);
            sac.setCreateSetValue(1L);
            sac.setReadMapOnModify(true);
            sac.setModifyAction(ModifyAction.INCREMENT);
        } else {
            sac = new CacheAttributeMapConfiguration(a, true, false, true, true);
            sac.setReadMapOnCreate(true);
            sac.setCreateAction(CreateAction.DEFAULT);
            sac.setReadMapOnModify(true);
            sac.setModifyAction(ModifyAction.DEFAULT);
        }

        return sac;
    }
}
