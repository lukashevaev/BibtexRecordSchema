package com.ols.record;

import com.sun.prism.impl.Disposer.Record;
import org.w3c.dom.Document;

import javax.ejb.Remote;
@Remote
public interface RecordSchema {
    String getURI();

    String toString(Record record, String encoding) throws Exception;

    Document toDocument(Record record, String encoding) throws Exception;

    Record normalize(Record record, String encoding);

    Record denormalize(Record record, String encoding);


}
