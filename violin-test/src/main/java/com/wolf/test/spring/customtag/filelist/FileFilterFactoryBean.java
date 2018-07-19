package com.wolf.test.spring.customtag.filelist;

import org.apache.commons.io.filefilter.NameFileFilter;
import org.springframework.beans.factory.FactoryBean;

import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Description:
 * <br/> Created on 2018/6/28 18:22
 *
 * @author 李超
 * @since 1.0.0
 */
public class FileFilterFactoryBean implements FactoryBean<Collection<FileFilter>> {

    private final List<FileFilter> filters = new ArrayList<FileFilter>();

    @Override
    public Collection<FileFilter> getObject() throws Exception {
        return filters;
    }

    @Override
    public Class<?> getObjectType() {
        return Collection.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    /**
     * Go through the list of filters and convert the String ones
     * (the ones that were set with <value> and make them NameFileFilters
     */
    public void setFilters(Collection<Object> filterList) {
        for (Object o : filterList) {
            if (o instanceof String) {
                filters.add(new NameFileFilter(o.toString()));
            } else if (o instanceof FileFilter) {
                filters.add((FileFilter) o);
            }
        }
    }

}