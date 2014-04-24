package com.cardiodata.json;

import com.cardiodata.core.jpa.CardioDataItem;
import com.cardiodata.core.jpa.CardioSession;
import java.util.List;

/**
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
public class CardioSessionWithData extends CardioSession {

    protected List<CardioDataItem> dataItems;

    public CardioSessionWithData(List<CardioDataItem> dataItems, Long id, String name, String description, Long serverId, Long userId, Long creationTimestamp, String dataClassName, Long originalSessionId, Long lastModificationTimestamp) {
        super(id, name, description, serverId, userId, creationTimestamp, dataClassName, originalSessionId, lastModificationTimestamp);
        this.dataItems = dataItems;
    }

    public CardioSessionWithData() {
    }

    public List<CardioDataItem> getDataItems() {
        return dataItems;
    }

    public void setDataItems(List<CardioDataItem> dataItems) {
        this.dataItems = dataItems;
    }

}
