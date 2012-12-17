package edu.iu.sci2.reader.flickr;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class FlickrImageGainer {
	public static final String base = "http://www.flickr.com/services/rest/";
	public static final int GET = 0;
	public static final int POST = 1;
	private final String apikey;
	
	public FlickrImageGainer(String apikey) {
		this.apikey = apikey;
	}

	public Map<String, List<String>> getImageURLs(String tag, List<String> uIDs)
			throws FlickrRuntimeException {
		try {
			return getImageURLs(tag,uIDs,"500");
		} catch (DocumentException e) {
			throw new FlickrRuntimeException("Fail to parse document from Flickr.");
		} catch (IOException e) {
			throw new FlickrRuntimeException("Cannot connect to Flickr.");
		}
	}

	private Map<String, List<String>> getImageURLs(String tag, List<String> uIDs,
			String pageContant) throws IOException, DocumentException {

		Map<String, List<String>> uidToUrlsMap = new HashMap<String, List<String>>();
		for (String uID : uIDs) {
			List<String> urlList = getImageURLs(tag, uID, pageContant);

			if (!urlList.isEmpty()) {
				uidToUrlsMap.put(uID, urlList);
			}
		}
		
		return uidToUrlsMap;
	}
	
	public List<String> getImageURLs(String tag, String uIDs)
			throws FlickrRuntimeException {
		try {
			return getImageURLs(tag,uIDs,"500");
		} catch (DocumentException e) {
			throw new FlickrRuntimeException("Fail to parse document from Flickr.");
		} catch (IOException e) {
			throw new FlickrRuntimeException("Cannot connect to Flickr.");
		}
	}
	
	private List<String> getImageURLs(String tag, String uID,
			String pageContant) throws IOException, DocumentException {

		String method = "flickr.photos.search";
		List<String> urlList = new ArrayList<String>();
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("tags", tag);
		parameters.put("user_id", uID);
		parameters.put("per_page", pageContant);
		Flickr flickr = new Flickr(base, apikey, method, parameters);

		InputStream in = flickr.callMethod(POST);
		SAXReader reader = new SAXReader();
		Document document = reader.read(in);
		Element rsp = document.getRootElement();
		Attribute state = rsp.attribute("stat");
		if (state.getValue().equals("ok")) {
			Element photos = rsp.element("photos");
			Iterator<Element> it = photos.elementIterator();
			while (it.hasNext()) {
				Element photo = it.next();
				String photoId = photo.attribute("id").getValue();
				urlList.add(getPhotoURL(photoId));
			}
			Attribute pages = photos.attribute("pages");
			int pagenume = Integer.parseInt(pages.getValue());
			if (pagenume > 1) {
				for (int page = 2; page <= pagenume; page++) {
					parameters.put("page", String.valueOf(page));
					flickr = new Flickr(base, apikey, method,
							parameters);
					in = flickr.callMethod(POST);
					try {
						document = reader.read(in);
						Element rsp1 = document.getRootElement();
						Attribute state1 = rsp1.attribute("stat");
						if (state1.getValue().equals("ok")) {
							Element photos1 = rsp.element("photos");
							Iterator<Element> it1 = photos1
									.elementIterator();
							while (it1.hasNext()) {
								Element photo = it.next();
								String photoId = photo.attribute("id")
										.getValue();
								urlList.add(getPhotoURL(photoId));
							}
						}
					} catch (DocumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		} else {
			throw new FlickrRuntimeException("Flickr request is fail!");
		}

		if (in != null) {
			in.close();
			in = null;
		}
		
		return urlList;
	}

	private String getPhotoURL(String photoId) throws IOException, DocumentException {
		String method = "flickr.photos.getInfo";
		String url = "";
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("photo_id", photoId);
		Flickr flickr = new Flickr(base, apikey, method, parameters);
		
		InputStream in = flickr.callMethod(POST);
		Document document = new SAXReader().read(in);
		Element rsp = document.getRootElement();
		Attribute state = rsp.attribute("stat");
		if (state.getValue().equals("ok")) {
			Element photoUrl = rsp.element("photo").element("urls")
					.element("url");
			url = photoUrl.getText();
		}
		
		if (in != null) {
			in.close();
		}
		return url;

	}

}
