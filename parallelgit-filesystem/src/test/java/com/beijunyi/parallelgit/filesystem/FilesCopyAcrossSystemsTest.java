package com.beijunyi.parallelgit.filesystem;

import java.io.IOException;
import java.nio.file.Files;

import org.eclipse.jgit.lib.Repository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FilesCopyAcrossSystemsTest extends AbstractGitFileSystemTest {

  private Repository targetRepo;
  private GitFileSystem targetGfs;

  @Before
  public void setupTargetSystem() throws IOException {
    targetRepo = new TestRepository();
    targetGfs = Gfs.newFileSystem(targetRepo);
    initRepository();
  }

  @After
  public void closeTargetSystem() throws IOException {
    targetGfs.close();
    targetRepo.close();
  }

  @Test
  public void copyFileToAnotherSystem_theTargetFileShouldExist() throws IOException {
    writeToCache("/source.txt");
    commitToMaster();
    initGitFileSystem();

    GitPath source = gfs.getPath("/source.txt");
    GitPath target = targetGfs.getPath("/target.txt");
    Files.copy(source, target);
    assertTrue(Files.exists(target));
  }

  @Test
  public void copyFileToAnotherSystem_theTargetFileShouldHaveTheSameData() throws IOException {
    byte[] expected = someBytes();
    writeToCache("/source.txt", expected);
    commitToMaster();
    initGitFileSystem();

    GitPath source = gfs.getPath("/source.txt");
    GitPath target = targetGfs.getPath("/target.txt");
    Files.copy(source, target);
    assertArrayEquals(expected, Files.readAllBytes(target));
  }

  @Test
  public void copyFileToAnotherSystem_theTargetFileSystemShouldBecomeDirty() throws IOException {
    writeToCache("/source.txt");
    commitToMaster();
    initGitFileSystem();


    GitPath source = getPathHelper(gfs,"/source.txt");
    GitPath target = getPathHelper(targetGfs,"/target.txt");
    Files.copy(source, target);
    assertTrue(getStatusHelper().isDirty());
  }

  @Test
  public void copyDirectoryToAnotherSystem_theTargetDirectoryShouldExist() throws IOException {
    writeToCache("/source/file.txt");
    commitToMaster();
    initGitFileSystem();

    GitPath source = gfs.getPath("/source");
    GitPath target = targetGfs.getPath("/target");
    Files.copy(source, target);
    assertTrue(Files.exists(target));
  }

  @Test
  public void copyDirectoryToAnotherSystem_theTargetDirectoryShouldHaveTheSameChildren() throws IOException {
    writeToCache("/source/file1.txt");
    writeToCache("/source/file2.txt");
    commitToMaster();
    initGitFileSystem();

    GitPath source = gfs.getPath("/source");
    GitPath target = targetGfs.getPath("/target");
    Files.copy(source, target);
    assertTrue(Files.exists(target.resolve("file1.txt")));
    assertTrue(Files.exists(target.resolve("file2.txt")));
  }

  @Test
  public void copyDirectoryToAnotherSystem_theChildrenInTheTargetDirectoryShouldHaveTheSameData() throws IOException {
    byte[] expectedData1 = someBytes();
    writeToCache("/source/file1.txt", expectedData1);
    byte[] expectedData2 = someBytes();
    writeToCache("/source/file2.txt", expectedData2);
    commitToMaster();
    initGitFileSystem();

    GitPath source = gfs.getPath("/source");
    GitPath target = targetGfs.getPath("/target");
    Files.copy(source, target);
    assertArrayEquals(expectedData1, Files.readAllBytes(target.resolve("file1.txt")));
    assertArrayEquals(expectedData2, Files.readAllBytes(target.resolve("file2.txt")));
  }

  @Test
  public void copyDirectoryToAnotherSystem_theTargetFileSystemShouldBecomeDirty() throws IOException {
    writeToCache("/source/file.txt");
    commitToMaster();
    initGitFileSystem();

    GitPath source = getPathHelper(gfs,"/source");
    GitPath target = getPathHelper(targetGfs,"/target");
    Files.copy(source, target);
    assertTrue(getStatusHelper().isDirty());
  }
  //Two helper/getter method to directly interact with GitFileSystem so that copyFileToAnotherSystem_theTargetFileSystemShouldBecomeDirty
  //and copyDirectoryToAnotherSystem_theTargetFileSystemShouldBecomeDirty Test does not cause feature envy code smell.

  public GfsStatusProvider getStatusHelper() {
    return targetGfs.getStatusProvider();
  }

  public GitPath getPathHelper(GitFileSystem xyz, String Path) {
    GitPath path = xyz.getPath(Path);
    return path;
  }
}
