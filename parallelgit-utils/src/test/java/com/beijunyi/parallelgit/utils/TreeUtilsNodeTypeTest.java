package com.beijunyi.parallelgit.utils;

import java.io.IOException;

import com.beijunyi.parallelgit.AbstractParallelGitTest;
import org.eclipse.jgit.lib.AnyObjectId;
import org.junit.Before;
import org.junit.Test;

import static org.eclipse.jgit.lib.FileMode.*;
import static org.junit.Assert.*;


public class TreeUtilsNodeTypeTest extends AbstractParallelGitTest {

  @Before
  public void setUp() throws IOException {
    initRepository();
  }

  @Test
  public void testIsDirectoryOnFile_shouldReturnFalse() throws IOException {
    writeToCache("/test_file.txt");
    AnyObjectId rootTree = commitToMaster().getTree();
    assertFalse(TreeUtilsExtended.isDirectory("/test_file.txt", rootTree, repo));
  }

  @Test
  public void testIsDirectoryOnDirectory_shouldReturnTrue() throws IOException {
    writeToCache("/test_dir/some_file.txt");
    AnyObjectId rootTree = commitToMaster().getTree();
    assertTrue(TreeUtilsExtended.isDirectory("/test_dir", rootTree, repo));
  }

  @Test
  public void testIsDirectoryWhenDirectoryDoesNotExist_shouldReturnFalse() throws IOException {
    writeSomethingToCache();
    AnyObjectId rootTree = commitToMaster().getTree();
    assertFalse(TreeUtilsExtended.isDirectory("/non_existent_directory", rootTree, repo));
  }

  @Test
  public void testIsRegularFileOnRegularFile_shouldReturnTrue() throws IOException {
    writeToCache("/test_file.txt");
    AnyObjectId rootTree = commitToMaster().getTree();
    assertTrue(TreeUtilsExtended.isFile("/test_file.txt", rootTree, repo));
  }

  @Test
  public void testIsRegularFileOnExecutableFile_shouldReturnTrue() throws IOException {
    writeToCache("/test_file.txt", someBytes(), EXECUTABLE_FILE);
    AnyObjectId rootTree = commitToMaster().getTree();
    assertTrue(TreeUtilsExtended.isFile("/test_file.txt", rootTree, repo));
  }

  @Test
  public void testIsRegularFileWhenFileDoesNotExist_shouldReturnFalse() throws IOException {
    writeSomethingToCache();
    AnyObjectId rootTree = commitToMaster().getTree();
    assertFalse(TreeUtilsExtended.isFile("/non_existent_file.txt", rootTree, repo));
  }

  @Test
  public void testIsSymbolicLinkOnRegularFile_shouldReturnFalse() throws IOException {
    writeToCache("/test_file.txt");
    AnyObjectId rootTree = commitToMaster().getTree();
    assertFalse(TreeUtilsExtended.isSymbolicLink("/test_file.txt", rootTree, repo));
  }

  @Test
  public void testIsSymbolicLinkOnSymbolicLink_shouldReturnTrue() throws IOException {
    writeToCache("/test_file.txt", someBytes(), SYMLINK);
    AnyObjectId rootTree = commitToMaster().getTree();
    assertTrue(TreeUtilsExtended.isSymbolicLink("/test_file.txt", rootTree, repo));
  }

  @Test
  public void testIsSymbolicLinkWhenFileDoesNotExist_shouldReturnFalse() throws IOException {
    writeSomethingToCache();
    AnyObjectId rootTree = commitToMaster().getTree();
    assertFalse(TreeUtilsExtended.isSymbolicLink("/non_existent_file.txt", rootTree, repo));
  }


}
