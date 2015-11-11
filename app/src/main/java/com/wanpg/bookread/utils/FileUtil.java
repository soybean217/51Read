package com.wanpg.bookread.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.exception.RarException;
import de.innosystec.unrar.rarfile.FileHeader;

/**
 * 一个文件操作的工具类
 *
 * @author wanpg
 */
public class FileUtil {
    
    public static final String localTypes = "epub,txt";
    public static final String downTypes = "epub,txt,rar,zip";
    
	public static void createFileIfNotExist(String path){
		File f = new File(path);
		if(!f.exists() && !f.isDirectory())
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public static boolean createFileIfNotExist(File f){
		if(!f.exists()){
			try {
				f.createNewFile();
                return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return false;
        } else {
          return true;
        }
	}
	
	public static void createFolderIfNotExist(String path){
		File f = new File(path);
		if(!f.exists())
			f.mkdirs();
	}
	
	public static void createFolderIfNotExist(File f){
		if(!f.exists())
			f.mkdirs();
	}
	
	
	/**
     * 将所得的InputStream，复制到指定目标的文件中
     *
     * @param inSource
     * @param targetFile
     * @return
     * @throws IOException
     */
    public static boolean copyStreamFile(InputStream inSource, File targetFile) throws IOException {
        if (inSource == null) {
            return false;
        }

        boolean isOk = false;
        BufferedOutputStream bufOut = null;
        File parent = new File(targetFile.getParent());
        if (!parent.exists()) {
            parent.mkdirs();
        }

        try {
            bufOut = new BufferedOutputStream(new FileOutputStream(targetFile));

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inSource.read(b)) != -1) {
                bufOut.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            bufOut.flush();
            isOk = true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            isOk = false;
            throw e;
        } finally {
            if (bufOut != null) {
                bufOut.close();
                bufOut = null;
            }
            if (inSource != null) {
                inSource.close();
                inSource = null;
            }
        }

        return isOk;
    }

    /**
     * 复制文件
     *
     * @param sourceFile
     * @param targetFile
     * @return
     */
    public static boolean copyFile(File sourceFile, File targetFile) {
        boolean isOk = false;
        BufferedInputStream bufIn = null;
        BufferedOutputStream bufOut = null;
        try {
            bufIn = new BufferedInputStream(new FileInputStream(sourceFile));
            bufOut = new BufferedOutputStream(new FileOutputStream(targetFile));
            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = bufIn.read(b)) != -1) {
                bufOut.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            bufOut.flush();
            isOk = true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            isOk = false;
        } finally {
            try {
                if (bufOut != null) {
                    bufOut.close();
                }
                if (bufIn != null) {
                    bufIn.close();
                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return isOk;
    }

    /**
     * 复制文件夹
     *
     * @param sourceFolder
     * @param targetFolder
     * @return
     */
    public static boolean copyFolder(String sourceFolder, String targetFolder) {
        boolean isOk = false;
        File sf = new File(sourceFolder);
        if (sf.isDirectory()) {
            String s[] = sf.list();
            // 新建目标目录
            (new File(targetFolder)).mkdirs();
            if (s.length > 0) {
                for (int i = 0; i < s.length; i++) {
                    File f1 = new File(sourceFolder + "/" + s[i]);
                    File f2 = new File(targetFolder + "/" + s[i]);
                    if (copyFile(f1, f2)) {

                    }
                }
            }
            isOk = true;
        }
        return isOk;
    }

    /**
     * 删除文件(夹)
     *
     * @param f
     */
    public static long del(File f) {
        long a = 0;
        if (f.exists()) {
            a = f.length();
            if (f.isDirectory()) {
                File[] files = f.listFiles();
                for (File f1 : files) {
                    a = a + del(f1);
                }
            }
            f.delete();
        }
        return a;
    }

    /**
     * 删除给定文件的子文件
     *
     * @param folder
     */
    public static long delFolderChild(File folder) {
        long a = 0;
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            for (File f : files) {
                a = a + del(f);
            }
        }
        return a;
    }

    /**
     * 检查文件是否是所需要的类型
     *
     * @return
     */
    public static boolean checkFileIsTarget(String fileName, String targetFormat) {
        String regEx = targetFormat + "$"; //表示a或f
        if (Pattern.compile(regEx, Pattern.CASE_INSENSITIVE).matcher(fileName).find()) {
            return true;
        }
        return false;
    }

    /**
     * 检查文件是否是可以打开的，用于本地打开的文件
     *
     * @param fileName
     * @return
     */
    public static boolean checkLocalFileIsCouldOpen(String fileName) {
        String[] sRegexs = localTypes.split(",");

        for (String s1 : sRegexs) {
            String regEx = s1 + "$";
            if (Pattern.compile(regEx, Pattern.CASE_INSENSITIVE).matcher(fileName).find()) {
                return true;
            }
        }
        return false;
    }


    /**
     * 检查文件是否是可以打开的，用于本地打开的文件
     *
     * @param fileName
     * @return
     */
    public static boolean checkDownloadIsOk(String fileName) {
        String[] sRegexs = downTypes.split(",");

        for (String s1 : sRegexs) {
//            String regEx = s1 + "$";
//            if (Pattern.compile(regEx, Pattern.CASE_INSENSITIVE).matcher(fileName).find()) {
//                return true;
//            }
        	if(fileName.endsWith(s1)){
        		return true;
        	}
        }
        return false;
    }



    /**
     * 判断文件是那种类型
     *
     * @param fileName
     * @return epub, txt, rar, zip
     */
    public static String getFileType(String fileName) {

        String[] sRegexs = downTypes.split(",");
        for (String s1 : sRegexs) {
            String regEx = s1 + "$";
            if (Pattern.compile(regEx, Pattern.CASE_INSENSITIVE).matcher(fileName).find()) {
                return s1;
            }
        }
        return "";
    }

    /**
     * 得到文件的编码格式
     *
     * @param b
     * @return
     */
    public static String getCharsetType(byte[] b) {
        String charset = "";
        byte[] codehead = new byte[4];
        // 截取数组
        System.arraycopy(b, 0, codehead, 0, 4);
        //LE [0xFF, 0xFE], BE [0xFE, 0xFF]
        if (codehead[0] == -1 && codehead[1] == -2) {
            charset = "UTF-16LE";
        } else if (codehead[0] == -2 && codehead[1] == -1) {
            charset = "UTF-16BE";
        } else if (codehead[0] == -17 && codehead[1] == -69 && codehead[2] == -65) {
            charset = "UTF-8";
        } else {
            charset = "GBK";
        }
        return charset;
    }


    /**
     * 检查rar文件里的是不是所要的文件
     *
     * @param rarPath
     * @param innerFileType
     * @return
     */
    public static boolean checkRarHasTarget(String rarPath, String innerFileType) {
        Archive a = null;
        try {
            a = new Archive(new File(rarPath));
            List<FileHeader> listHeader = a.getFileHeaders();
            for (FileHeader fh : listHeader) {
                if (checkFileIsTarget(fh.getFileNameW(), innerFileType)) {
                    return true;
                }
            }
        } catch (RarException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (a != null) {
                    a.close();
                    a = null;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return false;
    }


    /**
     * 检查zip文件里是否有所有的类型
     *
     * @param zipPath
     * @param innerFileType
     * @return
     */
    public static boolean checkZipHasTarget(String zipPath, String innerFileType) {
        ZipFile zf = null;

        try {
            zf = new ZipFile(zipPath);
            Enumeration<ZipEntry> zes = zf.getEntries();
            while (zes.hasMoreElements()) {
                ZipEntry ze = zes.nextElement();
                if (checkFileIsTarget(ze.getName(), innerFileType)) {
                    return true;
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } finally {

            try {
                if (zf != null) {
                    zf.close();
                    zf = null;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return false;

    }

    /**
     * @param path
     * @param name
     * @return
     * @throws Exception
     */
    public static boolean getRarContentFile(String path, String name) {
        Archive a = null;
        FileOutputStream fos = null;
        try {
            a = new Archive(new File(path));
            List<FileHeader> listHeader = a.getFileHeaders();
            for (FileHeader fh : listHeader) {
                if (fh.getFileNameW().contains(name)) {
                    if (!fh.isDirectory()) {
                        fos = new FileOutputStream(new File("c:/" + fh.getFileNameW()));
                        a.extractFile(fh, fos);
                        fos.flush();
                        break;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } finally {
            //此处关闭流
            try {
                if (fos != null) {
                    fos.close();
                    fos = null;
                }
                if (a != null) {
                    a.close();
                    a = null;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 解压zip文件
     *
     * @param path
     * @param name
     * @return
     */
    public static boolean getZipContentFile(String path, String name) {
        System.out.println("解压开始" + new Date().toString());
        ZipFile zipFile = null;
        InputStream ins = null;
        try {
            zipFile = new ZipFile(path, getFileEncoding(path));

            Enumeration<? extends ZipEntry> eZe = zipFile.getEntries();
            ZipEntry ze = eZe.nextElement();
            while (ze != null) {
                if (ze.getName().contains(name)) {
                    if (!ze.isDirectory()) {
                        break;
                    }
                }
                ze = eZe.nextElement();
            }
            ins = zipFile.getInputStream(ze);
            System.out.println("解压复制开始" + new Date().toString());
            copyStreamFile(ins, new File("c:\\" + ze.getName()));
            System.out.println("解压复制结束" + new Date().toString());
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ins != null) {
                    ins.close();
                    ins = null;
                }
                if (zipFile != null) {
                    zipFile.close();
                    zipFile = null;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 得到文件的编码格式
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static String getFileEncoding(String path) throws IOException {
        InputStream is = new FileInputStream(new File(path));
        String charset = "";
        byte[] codehead = new byte[4];
        // 截取数组
        is.read(codehead, 0, 4);
        is.close();
        //System.arraycopy(b, 0, codehead, 0, 4);
        //LE [0xFF, 0xFE], BE [0xFE, 0xFF]
        if (codehead[0] == -1 && codehead[1] == -2) {
            charset = "UTF-16LE";
        } else if (codehead[0] == -2 && codehead[1] == -1) {
            charset = "UTF-16BE";
        } else if (codehead[0] == -17 && codehead[1] == -69 && codehead[2] == -65) {
            charset = "UTF-8";
        } else {
            charset = "GBK";
        }
        return charset;
    }


    /**
     * 读二进制文件
     *
     * @throws IOException
     */
    public static byte[] readFileByBytes(String fileName) throws IOException {
        File file = new File(fileName);
        InputStream in = null;
        ByteArrayOutputStream bos = null;
        byte[] buffer;
        try {
            in = new FileInputStream(file);
            bos = new ByteArrayOutputStream();
            byte[] buf = new byte[10240 * 4]; //40k
            int len = 0;
            while ((len = in.read(buf)) > 0) {
                bos.write(buf, 0, len);
            }
            buffer = bos.toByteArray();
        } catch (IOException e) {
            throw e;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                }
            }
        }
        return buffer;
    }

    
}
