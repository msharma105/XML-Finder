import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utility {

	public static void main(String argv[]) {
		final String directoryName = "C:\\Users\\msh104\\Desktop\\Input";
		final String baseOutputPath="C:\\Users\\msh104\\Desktop\\Input\\david\\output\\harsha";
		List<String> tradeIDToGet=new ArrayList<String>();
		
		String[] trades=new String[] {"3574249"};
		//tradeIDToGet.add("3519560,3529618,3529619,3529633,3529634,3529638,3529628,3529635,3529629,3529651,3529663,3529669,3529670,3529675,3529692,3529681");
		tradeIDToGet=new ArrayList<String>(Arrays.asList(trades));
		List<File> fList = new ArrayList<File>();
		File fXmlFile;
		listf(directoryName, fList);

		Map<String, List<File>> idToFileMap = new HashMap<String, List<File>>();
		String tradeId;
		for (int i = 0; i < fList.size(); i++) {
			fXmlFile = fList.get(i);
			tradeId = giveTradeIDOfFile(fXmlFile);
			List<File> existingFile = new ArrayList<File>();
			if (idToFileMap.containsKey(tradeId)) {
				existingFile = idToFileMap.get(tradeId);
			}
			existingFile.add(fXmlFile);
			idToFileMap.put(tradeId, existingFile);
		}
		
		for (Map.Entry<String, List<File>> entry : idToFileMap.entrySet())
		{
			boolean move=false;
			String tradeID=entry.getKey();
		    System.out.print(entry.getKey()+"\t");
		    if(tradeIDToGet.contains(entry.getKey())) {
		    	move=true;
		    	System.out.println("will move\t");
		    }
		    int i=0;
		    for (File file : entry.getValue()) {
		    	System.out.println(file.getAbsolutePath()+"\t");
		    	if(move) {
		    		try {
		    			String postFix="";
		    			if(i!=0) postFix+=("_"+(i-1));
						moveAndRenameFile(file,tradeID+postFix,baseOutputPath);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		i++;
		    	}
		    }
		}

	}

	public static void listf(String directoryName, List<File> files) {
		File directory = new File(directoryName);

		try {
			// Get all files from a directory.
			File[] fList = directory.listFiles();
			if (fList != null)
				for (File file : fList) {
					if (file.isFile()) {
						files.add(file);
					} else if (file.isDirectory()) {
						listf(file.getAbsolutePath(), files);
					}
				}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static String giveTradeIDOfFile(File fXmlFile) {
		String tradeId = "";
		try {
			// File fXmlFile = new
			// File("C:\\Users\\msh104\\Desktop\\2018\\1\\1\\0\\GBM.TMS_MRX.SAPIENT.INT_RATE_SWAP_2d08ab53-56b1-4e99-a75b-e97617a32cff_23102018_131428621.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("contractId");
			Node nNode = nList.item(0);
			Element eElement = (Element) nNode;
			tradeId = eElement.getElementsByTagName("rootContract").item(0).getTextContent();
			//System.out.println("Trade ID : " + tradeId);
		} catch (Exception e) {

		}
		return tradeId;
	}
	
	public static void moveAndRenameFile(File file,String tradeId,String baseOutputPath) throws IOException {
		//Files.move(file.getPath(),Paths.get(baseOutputPath+tradeId+".xml") , java.nio.file.StandardCopyOption.REPLACE_EXISTING);
		//File file = new File("/Users/pankaj/java.txt");
        File newFile = new File(baseOutputPath+"/"+tradeId+".xml");
//        System.out.println("path : " + newFile.getPath());
//        if(file.renameTo(newFile)){
//            System.out.println("File rename success");;
//        }else{
//            System.out.println("File rename failed");
//       }
        copyFileUsingStream(file,newFile);
	}
	
	private static void copyFileUsingStream(File source, File dest) throws IOException {
	    InputStream is = null;
	    OutputStream os = null;
	    try {
	        is = new FileInputStream(source);
	        os = new FileOutputStream(dest);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	    } finally {
	        is.close();
	        os.close();
	    }
	}
}
