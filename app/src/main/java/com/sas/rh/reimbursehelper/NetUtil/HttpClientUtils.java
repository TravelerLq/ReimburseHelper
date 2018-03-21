package com.sas.rh.reimbursehelper.NetUtil;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * 上传工具类
 */
public class HttpClientUtils {

	/**
	 * @param actionUrl
	 *            上传的地址
	 * @param params
	 *            上传的键值对参数
	 * @param filePath
	 *            上传的图片路径
	 */
	//上传方法1，带一或多张图片
	public static JSONObject shangchuan1(String url, Map<String, Object> map, String... strings)
			throws UnsupportedEncodingException {
		JSONObject ja = JSONObject.fromObject(map);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("data", String.valueOf(ja));
		return HttpClientUtils.post(url, params, strings);
	}
	//上传方法2，不带图片
	public static JSONObject shangchuan2(String url, Map<String, Object> map) throws UnsupportedEncodingException {
		JSONObject ja = JSONObject.fromObject(map);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("data", String.valueOf(ja));
		return HttpClientUtils.post(url, params);
	}
	
	static JSONObject post(String actionUrl, Map<String, Object> params, String... strings) {

		try {
			// 设置报头信息
			String BOUNDARY = "--------------et567z"; // 数据分隔线
			String MULTIPART_FORM_DATA = "multipart/form-data";
			URL url = new URL(actionUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);// 允许输入
			conn.setDoOutput(true);// 允许输出
			conn.setUseCaches(false);// 不使用Cache
			conn.setReadTimeout(5000);
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Content-Type", MULTIPART_FORM_DATA + ";boundary=" + BOUNDARY);

			// 获取map对象里面的数据，并转化为string
			StringBuilder sb = new StringBuilder();
			System.out.println("上传名称:" + params.get("data"));
			// 上传的表单参数部分，不需要更改
			for (Map.Entry<String, Object> entry : params.entrySet()) {// 构建表单字段内容
				sb.append("--");
				sb.append(BOUNDARY);
				sb.append("\r\n");
				sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
				sb.append(entry.getValue());
				sb.append("\r\n");
			}
			DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
			outStream.write(sb.toString().getBytes());// 发送表单字段数据

			
//			判断是否需要上传图片
			if (strings.length != 0) {
				// 上传图片部分
				for (String string : strings) {
					shangChuanTuPian(string, outStream);
				}
			}
			
//			数据结束标识符
			byte[] end_data = ("--" + BOUNDARY + "--\r\n").getBytes();
			outStream.write(end_data);
			outStream.flush();

			// 返回状态判断
			int cah = conn.getResponseCode();
			if (cah == 200)// 如果发布成功则提示成功
			{
				System.out.println("上传成功");
				// 回调方法
				JSONObject jsonObject = getRuturnJson(conn);
				return jsonObject;
//				System.out.println("ResultCode:" + jsonObject.get("ResultCode") + "\t" + "HostTime:"
//						+ jsonObject.get("HostTime") + "\t" + "Note:" + jsonObject.get("Note"));
//				if (jsonObject.get("resultList")!= null) {
//					System.out.print("resultList:");
//					JSONArray jsonArray = jsonObject.getJSONArray("resultList");
//					for (Object object : jsonArray) {
//						JSONObject jObject = JSONObject.fromObject(object);
//						System.out.println(jObject);
//					}
//				}
			} else if (cah == 400) {
				System.out.println("400错误");
			} else {
				throw new RuntimeException("请求url失败:" + cah);
			}

			outStream.close();
			conn.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 上传图片
	private static void shangChuanTuPian(String filePath, DataOutputStream outStream) throws IOException {
		String BOUNDARY = "--------------et567z"; // 数据分隔线
		// 调用自定义方法获取图片文件的byte数组
		byte[] content = readFileImage(filePath);
		System.out.println("图片大小：" + content.length);
		// 再次设置报头信息
		StringBuilder split = new StringBuilder();
		split.append("--");
		split.append(BOUNDARY);
		split.append("\r\n");

		split.append("Content-Disposition: form-data;name=\"file\";filename=\"" + filePath + "\"\r\n");
		// 这里的Content-Type非常重要，一定要是图片的格式，例如image/jpeg或者image/jpg
		// 服务器端对根据图片结尾进行判断图片格式到底是什么,因此务必保证这里类型正确
		String houzhui = pctForm(filePath);
		split.append("Content-Type: image/" + houzhui + "\r\n\r\n");
		outStream.write(split.toString().getBytes());
		outStream.write(content, 0, content.length);
		outStream.write("\r\n".getBytes());
	}

	// 获取后缀名
	private static String pctForm(String filepath) {
		if (filepath.indexOf(".jpg") != 1) {
			return "jpg";
		} else if (filepath.indexOf(".png") != 1) {
			return "png";
		} else if (filepath.indexOf(".jpeg") != 1) {
			return "jpeg";
		}
		return "png";

	}

	@SuppressWarnings("resource")
	private static byte[] readFileImage(String filePath) throws IOException {

		BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(filePath));
		int len = bufferedInputStream.available();
		byte[] bytes = new byte[len];
		int r = bufferedInputStream.read(bytes);
		if (len != r) {
			bytes = null;
			throw new IOException("读取文件不正确");
		}
		bufferedInputStream.close();
		return bytes;
	}

	// 获取返回jsonObject
	private static JSONObject getRuturnJson(HttpURLConnection conn) throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[1024];
		int len = 0;
		InputStream inStream = conn.getInputStream();
		while ((len = inStream.read(data)) != -1) {
			outStream.write(data, 0, len);
		}
		inStream.close();
		System.out.println(new String(outStream.toByteArray()));
		JSONObject jsonObject = JSONObject.fromObject(new String(outStream.toByteArray()));
		return jsonObject;
	}
	//返回jsonArray
	@SuppressWarnings("unused")
	private static JSONArray getRuturnJson1(HttpURLConnection conn) throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[1024];
		int len = 0;
		InputStream inStream = conn.getInputStream();
		while ((len = inStream.read(data)) != -1) {
			outStream.write(data, 0, len);
		}
		inStream.close();
		System.out.println(new String(outStream.toByteArray()));
		JSONArray jsonArray = JSONArray.fromObject(new String(outStream.toByteArray()));
		return jsonArray;
	}
	
}