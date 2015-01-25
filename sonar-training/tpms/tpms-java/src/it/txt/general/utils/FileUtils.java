package it.txt.general.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileUtils {


    /**
     * Ritorna il nome del file senza estensione
     *
     * @param file
     * @return Ritorna il nome del file senza estensione
     */
    public static String filename(File file) {
        String name = file.getName();
        int dot = name.lastIndexOf('.');
        return (dot != -1) ? name.substring(0, dot) : name;
    }

    /**
     * Ritorna l'estensione di un file
     *
     * @param file
     * @return l'estensione di un file
     */
    public static String extension(File file) {
        String name = file.getName();
        int dot = name.lastIndexOf('.');
        return (dot != -1) ? name.substring(dot) : "";
    }

    /**
     * Cancella il contenuto di una directory (sottodirectory comprese)
     *
     * @param directory da cancellare.
     */
    public static void clearDirectory(File directory) {
        // Cancella il contenuto delle sottodirectory
        File[] dirs = directory.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        for (int i = 0; i < dirs.length; ++i) {
            File dir = dirs[i];
            clearDirectory(dir);
            dir.delete();
        }

        // Cancella il contenuto della directory corrente
        File[] files = directory.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.isFile();
            }
        });
        for (int i = 0; i < files.length; ++i) {
            files[i].delete();

        }
    }

    /**
     * Copia il contenuto di una directory (sourceDir) nella directory di destinazione into
     *
     * @param sourceDir
     * @param into
     * @throws IOException
     */
    public static void copyDirectory(File sourceDir, File into) throws IOException {
        File targetDir = new File(into, sourceDir.getName());
        targetDir.mkdirs();

        File[] list = sourceDir.listFiles();
        File f;
        for (int i = 0; i < list.length; i++) {
            f = list[i];
            if (f.isDirectory()) {
                copyDirectory(f, targetDir);
            } else {
                copy(f, targetDir);
            }
        }
    }

    /**
     * Copia il file indicato nel primo paramento, nel file o directory indicata
     * cope secondo parametro.
     * <p>Se il secondo parametro e' una directory, il file viene copiato con lo stesso
     * nome.
     * <p>Non vengono fatti dei controlli per sapere se il file di destinazione e' lo
     * stesso di quello di partenza
     *
     * @param fromFile file da copiare
     * @param toFile   file o directory su cui fare la copia
     * @throws IOException
     */
    public static void copy(String fromFile, String toFile) throws IOException {
        copy(new File(fromFile), new File(toFile));
    }

    /**
     * Copia il file indicato nel primo paramento, nel file o directory indicata
     * cope secondo parametro.
     * <p>Se il secondo parametro e' una directory, il file viene copiato con lo stesso
     * nome.
     * <p>Se il file di destinazione esiste, viene cancellato.
     * <p>Non vengono fatti dei controlli per sapere se il file di destinazione e' lo
     * stesso di quello di partenza
     *
     * @param fromFile file da copiare
     * @param toFile   file o directory su cui fare la copia
     * @throws IOException
     */
    public static void copy(File fromFile, File toFile) throws IOException {
        if (toFile.isDirectory()) {
            toFile = new File(toFile, fromFile.getName());

        }
        if (toFile.exists()) {
            toFile.delete();

        }
        InputStream in = new FileInputStream(fromFile);
        OutputStream out = new FileOutputStream(toFile);
        copy(in, out);
        in.close();
        out.flush();
        out.close();
    }

    /**
     * Copia l'input stream nell'output stream indicato.
     * <p>Usa il buffer istanziato nel costruttore
     *
     * @param in  input stream
     * @param out output stream su cui fare la copia
     * @throws IOException
     */
    public static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int size;
        while ((size = in.read(buffer)) > 0) {
            out.write(buffer, 0, size);
        }
    }

    public static File[] unzip(File zipFile, File dir) throws Exception {
        ArrayList fileList = new ArrayList();
        ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile));
        ZipEntry e;
        File file;
        while ((e = zin.getNextEntry()) != null) {
            file = unzip(zin, e.getName(), dir);
            fileList.add(file);
        }
        zin.close();
        return (File[]) fileList.toArray(new File[0]);

    }

    private static File unzip(ZipInputStream zin, String s, File dir) throws IOException {
        File file = new File(dir, s);
        FileOutputStream out = new FileOutputStream(file);
        byte[] b = new byte[512];
        int len;
        while ((len = zin.read(b)) != -1) {
            out.write(b, 0, len);
        }
        out.close();
        return file;
    }


    public static File zipDirectory(File dir, File zipFileDir, boolean deleteIfExists) throws Exception {
        zipFileDir.mkdirs();
        File zipFile = new File(zipFileDir, dir.getName() + ".zip");
        if (zipFile.exists() && deleteIfExists) {
            zipFile.delete();

        }
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));
        addDirToZip(dir, out, dir);
        out.close();
        return zipFile;
    }

    public static File zipDirectory(File dir, boolean deleteIfExists) throws Exception {
        return zipDirectory(dir, dir.getParentFile(), deleteIfExists);
    }

    private static void addDirToZip(File dir, ZipOutputStream out, File baseDir) throws Exception {
        File[] list = dir.listFiles();
        File f;
        byte[] buf = new byte[1024];
        for (int i = 0; i < list.length; i++) {
            f = list[i];
            if (f.isDirectory()) {
                addDirToZip(f, out, baseDir);
            } else {
                FileInputStream in = new FileInputStream(f);
                out.putNextEntry(new ZipEntry(getRelativePath(f, baseDir)));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
        }
    }

    public static String getRelativePath(File f, File baseDir) {
        String absPath = f.getAbsolutePath();
        int fromBase = absPath.indexOf(baseDir.getAbsolutePath()) + baseDir.getAbsolutePath().length() + 1;
        return absPath.substring(fromBase);
    }


    /**
     * Dato un file name comprensivo di percorso se necessario crea il percorso e il file.
     *
     * @param strPath   percorso e nome file da creare
     * @param overWrite se true sovrascrive il file se esistente
     * @return true se la creazione avviene con successo, false altrimenti
     * @throws IOException
     */
    public static boolean createPath(String strPath, boolean overWrite) throws IOException {

        String path = strPath.substring(0, strPath.lastIndexOf(File.separator) + 1);
        File dir = new File(path);
        boolean dirCreation = true;
        if (!dir.exists()) {
            dirCreation = dir.mkdirs();
        }
        boolean fileCreation = true;

        if (dirCreation) {
            File f = new File(strPath);
            if (overWrite && f.exists())
                f.delete();
            if (!f.exists())
                fileCreation = f.createNewFile();
        }

        return (fileCreation && dirCreation);

    }

    /**
     * Given a fileName (with fully qualified path) and a content (fileContent) write the content to the the file.
     * If the fileName doesn't exist create it and than write the content, if the file aready exists and overWrite is true
     * the file will be overwritten.
     *
     * @param fileName    the file name to erite to
     * @param fileContent the content of the file
     * @param overWrite   if true the file will be overwritten
     * @return true if all the operation are well terminated, false otherwise
     * @throws IOException if an error occours
     */
    public static boolean writeToFile(String fileName, String fileContent, boolean overWrite) throws IOException {
        boolean fileCreation;
        boolean append = false;
        if (!(new File(fileName).exists())) {
            fileCreation = createPath(fileName, overWrite);
        } else {
            append = true;
            fileCreation = true;
        }
        if (fileCreation) {
            PrintWriter out = null;
            try {
                out = new PrintWriter(new FileOutputStream(fileName, append));
                out.println(fileContent);
            } finally {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            }
        }

        return fileCreation;
    }

    /**
     * Write fileContent into a file identified by fileName, if the file already exists append filoeContent to it.
     *
     * @param fileName    file name (with fully qualified path) to write to
     * @param fileContent the content that should be written to fileName
     * @return true if all the operation are well terminated, false otherwise
     * @throws IOException if an error occours
     */
    public static boolean writeToFile(String fileName, String fileContent) throws IOException {
        return writeToFile(fileName, fileContent, false);
    }


    /**
     * @param filename
     * @return read a file ad return the content as String
     */
    public static StringBuffer readFile(String filename) throws IOException, FileNotFoundException {
        StringBuffer s;
        FileInputStream in = null;

        try {
            File file = new File(filename);
            byte[] buffer = new byte[(int) file.length()];
            in = new FileInputStream(file);
            in.read(buffer);
            s = new StringBuffer(new String(buffer));
            in.close();
        }
        finally {
            if (in != null) {
                in.close();
            }
        }
        return s;
    }

}
