package src.bin;
 
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
 
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
 
/**
 * 
* @ClassName: ReptileUtil 
* @Description: ͼƬ���� -- ʹ��jsoup����htmlҳ�棬��ȡ��Ҫ��·��������ѭ������
* @author zhoujie 
* @date 2018��7��27�� ����8:26:01 
* @version V1.0
* <a href="http://www.baidu.com">�ٶ�</a>
 */
public class ReptileUtil {
	
	//����·��
	static String baseurl = "http://www.netbian.com/";
	static String geturl = "http://www.netbian.com/desk/18321.htm";
//	
	static String filepath = "D:\\downlond\\test\\" ;
 
	public static void main(String[] args) {
		System.out.println("��ʼ����ҳ�棺"+geturl);
		String html = getHtml(geturl); //htmlҳ������
		List<String> srclists = getImgSrcListFromHtml(html); //ͼƬ��ַ����
		downloadImg(srclists, filepath); //����ͼƬ
		//��ȡ��һ��ҳ���������
		List<String> list = getNextPageUrl(html); 
		System.out.println(list.size());
		for (int i = 0; i < list.size(); i++) {
			String url = list.get(i);
			System.out.println("��һ������ҳ�棺"+url);
			String html2 = getHtml(url); //htmlҳ������
			List<String> srclists2 = getImgSrcListFromHtml(html2); //ͼƬ��ַ����
			downloadImg(srclists2, filepath); //����ͼƬ
		}
		System.out.println("�������");
	}
	
	/**
	 * 
	* @Title: getHtml 
	* @Description: ��ȡҳ������
	* @param @param url
	* @param @return  ҳ������
	* @return String  �������� 
	* @throws
	 */
	public static String getHtml(String url){
		String html = "";
		try {
			html = Jsoup.connect(url).execute().body();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return html;
	}
	
	/**
	 * 
	* @Title: getImgSrcListFromHtml 
	* @Description: ��ȡҳ������ͼƬ·��
	* @param @param html  ҳ������
	* @param @return    ͼƬ·������
	* @return ArrayList<String>    �������� 
	* @throws
	 */
	public static List<String> getImgSrcListFromHtml(String html){
		List<String> list = new ArrayList<>();
		//������htmlҳ��
		Document document = Jsoup.parse(html);
		//��ȡĿ��
		Elements elements = document.select("div [class=pic]").select("img");
		int len = elements.size();
		for (int i = 0; i < len; i++) {
			list.add(elements.get(i).attr("src"));
		}
		return list;
	}
	
	/**
	 * 
	* @Title: getNextPage 
	* @Description: ��ҳ�������л�ȡ��һ��ҳ��·��
	* @param     ҳ������
	* @return List<String>  ����ҳ��url����
	* @throws
	 */
	public static List<String> getNextPageUrl(String html){
		List<String> list = new ArrayList<>();
		//������htmlҳ��
		Document document = Jsoup.parse(html);
		//��ȡĿ��
		Elements elements = document.select("div [class=list]").select("a");
		for (int i = 0;i<elements.size();i++) {
			String url = baseurl + elements.get(i).attr("href");
			list.add(url);
		}
		return list;
	}
	
	/**
	 * 
	* @Title: downloadImg 
	* @Description: ����ͼƬ -- ͨ����ȡ����ת��byte[]���飬��ͨ��FileOutputStreamд��
	* @param @param list ͼƬ·������
	* @param @param filepath  �����ļ���λ��
	* @return void    �������� 
	* @throws
	 */
	public static void downloadImg(List<String> list, String filepath){
		URL newUrl = null;
		HttpURLConnection hconnection = null;
		InputStream inputStream = null;
		FileOutputStream fileOutputStream = null;
		byte[] bs = null;
		try {
			int len = list.size();
			for (int i = 0; i < len; i++) {
				newUrl = new URL(list.get(i));
				hconnection = (HttpURLConnection) newUrl.openConnection(); //������
				inputStream = hconnection.getInputStream();  //��ȡ��
				bs = getBytesFromInputStream(inputStream); //��תbtye[]
				filepath = filepath + list.get(i).substring(list.get(i).lastIndexOf("/")+1); //��ȡͼƬ����
				System.out.println("����ͼƬ·��:"+filepath);
				fileOutputStream = new FileOutputStream(new File(filepath));
				fileOutputStream.write(bs); //д��
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
				fileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	* @Title: getBytesFromInputStream 
	* @Description: InputStream��ת��byte[]
	* @param @param inputStream
	* @param @return    byte[]
	* @return byte[]    �������� 
	* @throws
	 */
	public static byte[] getBytesFromInputStream(InputStream inputStream){
		byte[] bs = null;
		try {
			byte[] buffer = new byte[1024];
			int len = 0;
			ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream(); //
			while((len = inputStream.read(buffer)) != -1){
				arrayOutputStream.write(buffer, 0 ,len);
			}
			bs = arrayOutputStream.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bs;
	}
	
}