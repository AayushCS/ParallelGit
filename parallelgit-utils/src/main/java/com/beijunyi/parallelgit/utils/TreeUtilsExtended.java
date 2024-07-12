package com.beijunyi.parallelgit.utils;

import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.treewalk.TreeWalk;

import javax.annotation.Nonnull;
import java.io.IOException;

import static com.beijunyi.parallelgit.utils.TreeUtils.forPath;
import static com.beijunyi.parallelgit.utils.TreeUtils.getFileMode;
import static org.eclipse.jgit.lib.FileMode.*;
import static org.eclipse.jgit.lib.FileMode.SYMLINK;

//New Class created which is an extension to TreeUtils, is responsible for some tasks of original treeUtil
//and prevents "insufficient modularization" code smell"
public class TreeUtilsExtended{

    @Nonnull
    public static TreeWalk newTreeWalk(AnyObjectId tree, ObjectReader reader) throws IOException {
        TreeWalk tw = new TreeWalk(reader);
        tw.reset(tree);
        return tw;
    }

    @Nonnull
    public static TreeWalk newTreeWalk(AnyObjectId tree, Repository repo) throws IOException {
        return newTreeWalk(tree, repo.newObjectReader());
    }
    public static boolean isDirectory(TreeWalk tw) {
        return TREE.equals(getFileMode(tw));
    }

    public static boolean isDirectory(String path, AnyObjectId tree, ObjectReader reader) throws IOException {
        try(TreeWalk tw = forPath(path, tree, reader)) {
            return tw != null && isDirectory(tw);
        }
    }

    public static boolean isDirectory(String path, AnyObjectId tree, Repository repo) throws IOException {
        try(ObjectReader reader = repo.newObjectReader()) {
            return isDirectory(path, tree, reader);
        }
    }

    public static boolean isFile(TreeWalk tw) {
        return REGULAR_FILE.equals(getFileMode(tw)) || EXECUTABLE_FILE.equals(getFileMode(tw));
    }

    public static boolean isFile(String path, AnyObjectId tree, ObjectReader reader) throws IOException {
        try(TreeWalk tw = forPath(path, tree, reader)) {
            return tw != null && isFile(tw);
        }
    }

    public static boolean isFile(String path, AnyObjectId tree, Repository repo) throws IOException {
        try(ObjectReader reader = repo.newObjectReader()) {
            return isFile(path, tree, reader);
        }
    }

    public static boolean isSymbolicLink(TreeWalk tw) {
        return SYMLINK.equals(getFileMode(tw));
    }

    public static boolean isSymbolicLink(String path, AnyObjectId tree, ObjectReader reader) throws IOException {
        try(TreeWalk tw = forPath(path, tree, reader)) {
            return tw != null && isSymbolicLink(tw);
        }
    }

    public static boolean isSymbolicLink(String path, AnyObjectId tree, Repository repo) throws IOException {
        try(ObjectReader reader = repo.newObjectReader()) {
            return isSymbolicLink(path, tree, reader);
        }
    }

    @Nonnull
    public static ObjectId insertTree(TreeFormatter tf, Repository repo) throws IOException {
        try(ObjectInserter inserter = repo.newObjectInserter()) {
            ObjectId treeId = inserter.insert(tf);
            inserter.flush();
            return treeId;
        }
    }

}
