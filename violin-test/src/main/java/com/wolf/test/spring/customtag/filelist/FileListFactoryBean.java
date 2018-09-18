package com.wolf.test.spring.customtag.filelist;

import com.wolf.utils.ArrayUtils;
import org.springframework.beans.factory.FactoryBean;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * Description:
 * <br/> Created on 2018/6/28 18:16
 *
 * @author 李超
 * @since 1.0.0
 */
public class FileListFactoryBean implements FactoryBean<Collection<File>> {

    String directory;
    private Collection<FileFilter> filters;
    private Collection<File> nestedFiles;

    @Override
    public Collection<File> getObject() throws Exception {
        // These can be an array list because the directory will have unique's and the nested is already only unique's
        Collection<File> files = new ArrayList<File>();
        Collection<File> results = new ArrayList<File>(0);

        if (directory != null) {
            // get all the files in the directory
            File dir = new File(directory);
            File[] dirFiles = dir.listFiles();
            if (dirFiles != null) {
                files = ArrayUtils.toList(dirFiles);
            }
        }

        // If there are any files that were created from the nested tags,
        // add those to the list of files
        if (nestedFiles != null) {
            files.addAll(nestedFiles);
        }

        // If there are filters we need to go through each filter
        // and see if the files in the list pass the filters.
        // If the files does not pass any one of the filters then it
        // will not be included in the list
        if (filters != null) {
            boolean add;
            for (File f : files) {
                add = true;
                for (FileFilter ff : filters) {
                    if (!ff.accept(f)) {
                        add = false;
                        break;
                    }
                }
                if (add) results.add(f);
            }
            return results;
        }

        return files;
    }

    @Override
    public Class<?> getObjectType() {
        return Collection.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    public void setDirectory(String dir) {
        this.directory = dir;
    }

    public void setFilters(Collection<FileFilter> filters) {
        this.filters = filters;
    }

    /**
     * What we actually get from the processing of the nested tags
     * is a collection of files within a collection so we flatten it and
     * only keep the uniques
     */
    public void setNestedFiles(Collection<Collection<File>> nestedFiles) {
        this.nestedFiles = new HashSet<File>(); // keep the list unique
        for (Collection<File> nested : nestedFiles) {
            this.nestedFiles.addAll(nested);
        }
    }

}