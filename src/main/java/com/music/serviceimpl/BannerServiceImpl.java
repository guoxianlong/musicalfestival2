package com.music.serviceimpl;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.music.dao.BannerDao;
import com.music.entity.Banner;
import com.music.service.BannerService;


@Service
public class BannerServiceImpl implements BannerService{
	//依赖持久层：注入
	@Autowired
	private BannerDao bannerDao;

	public int insertPhoto(String photo_url) {    //上传图片
		// TODO Auto-generated method stub
		return bannerDao.insertPhoto(photo_url);
	}

	public int updatePhoto_point_url(String photo_point_url,int id) {
		// TODO Auto-generated method stub
		return bannerDao.updatePhoto_point_url(photo_point_url,id);
	}

	public List<Banner> selectList() {
		// TODO Auto-generated method stub
		return bannerDao.selectList();
	}

	public int deleteOne(int id) {
		// TODO Auto-generated method stub
		return bannerDao.deleteOne(id);
	}

	public String SavePrdPic(MultipartFile multipartFile,int id,HttpServletRequest request) {
		
		URL url = this.getClass().getClassLoader().getResource("uploadPath.properties");
		Properties pro = new Properties();
		FileInputStream in;
		try {
			in = new FileInputStream(url.getPath());
			pro.load(in);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String filepath = pro.get("brandpath").toString();	
		String serverPath = request.getSession().getServletContext().getRealPath("/");
		File root=new File(serverPath);
		String savePath = root.getParent();
		String realPath = savePath+filepath;
		File dir = new File(realPath);		
        if (!dir.exists()) {
            dir.mkdirs();
        }
		
		String alias = UUID.randomUUID().toString().replaceAll("-", "");
		String filename = alias+multipartFile.getOriginalFilename();
		String filePathXiangDui = filepath+filename;	
		//System.out.println(filename+"filename");
		
        try {
            multipartFile.transferTo(new File(realPath, filename));
            Banner banner = bannerDao.selectOne(id) ;   //先按照id查询,如果没有数据插入，有数据更新
    		if(banner==null) {
    			int resDao = bannerDao.insertPhoto(filePathXiangDui);
                if(resDao>0) {
                	 return filePathXiangDui;
                }else {                	
                	return "error";
                }
    		}else {
    			int resDao = bannerDao.updatePhoto_url(filePathXiangDui, id);
    			if(resDao>0) {   				
               	 return filePathXiangDui;
               }else {            	   
               	return "error";
               }
    		}
           
        } catch (IOException e) {
            e.printStackTrace();
            
        }
        return "error";
	}

	
	
	
}
