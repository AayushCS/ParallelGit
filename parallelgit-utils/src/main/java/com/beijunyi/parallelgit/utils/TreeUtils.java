package com.beijunyi.parallelgit.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.NoSuchFileException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.beijunyi.parallelgit.utils.io.BlobSnapshot;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.treewalk.TreeWalk;

import static org.eclipse.jgit.lib.FileMode.*;

public class TreeUtils {

  @Nonnull
  public static String normalizeNodePath(String path) {
    if(path.startsWith("/"))
      return path.substring(1);
    if(path.endsWith("/"))
      return path.substring(0, path.length() - 1);
    return path;
  }

  @Nullable
  public static TreeWalk forPath(String path, AnyObjectId tree, ObjectReader reader) throws IOException {
    return TreeWalk.forPath(reader, normalizeNodePath(path), tree);
  }

  @Nullable
  public static TreeWalk forPath(String path, AnyObjectId tree, Repository repo) throws IOException {
    try(ObjectReader reader = repo.newObjectReader()) {
      return forPath(path, tree, reader);
    }
  }

  public static boolean exists(String path, AnyObjectId tree, ObjectReader reader) throws IOException {
    return forPath(path, tree, reader) != null;
  }

  public static boolean exists(String path, AnyObjectId tree, Repository repo) throws IOException {
    try(ObjectReader reader = repo.newObjectReader()) {
      return exists(path, tree, reader);
    }
  }

  @Nullable
  public static ObjectId getObjectId(TreeWalk tw) {
    return tw.getObjectId(0);
  }

  @Nullable
  public static ObjectId getObjectId(String path, AnyObjectId tree, ObjectReader reader) throws IOException {
    try(TreeWalk tw = forPath(path, tree, reader)) {
      return tw != null ? getObjectId(tw) : null;
    }
  }

  @Nullable
  public static ObjectId getObjectId(String path, AnyObjectId tree, Repository repo) throws IOException {
    try(ObjectReader reader = repo.newObjectReader()) {
      return getObjectId(path, tree, reader);
    }
  }

  @Nullable
  public static FileMode getFileMode(TreeWalk tw) {
    return tw.getFileMode(0);
  }

  @Nullable
  public static FileMode getFileMode(String path, AnyObjectId tree, ObjectReader reader) throws IOException {
    try(TreeWalk tw = forPath(path, tree, reader)) {
      return tw != null ? getFileMode(tw) : null;
    }
  }

  @Nullable
  public static FileMode getFileMode(String path, AnyObjectId tree, Repository repo) throws IOException {
    try(ObjectReader reader = repo.newObjectReader()) {
      return getFileMode(path, tree, reader);
    }
  }

  @Nonnull
  public static InputStream openFile(String path, AnyObjectId tree, ObjectReader reader) throws IOException {
    AnyObjectId blobId = getObjectId(path, tree, reader);
    if(blobId == null)
      throw new NoSuchFileException(path);
    return BlobUtils.openBlob(blobId, reader);
  }

  @Nonnull
  public static InputStream openFile(String path, AnyObjectId tree, Repository repo) throws IOException {
    try(ObjectReader reader = repo.newObjectReader()) {
      return openFile(path, tree, reader);
    }
  }

  @Nonnull
  public static BlobSnapshot readFile(String path, ObjectId tree, ObjectReader reader) throws IOException {
    ObjectId blobId = getObjectId(path, tree, reader);
    if(blobId == null)
      throw new NoSuchFileException(path);
    return BlobUtils.readBlob(blobId, reader);
  }

  @Nonnull
  public static BlobSnapshot readFile(String path, ObjectId tree, Repository repo) throws IOException {
    try(ObjectReader reader = repo.newObjectReader()) {
      return readFile(path, tree, reader);
    }
  }



}
