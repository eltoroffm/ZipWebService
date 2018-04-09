package com.restws.zipws;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.core.MediaType;

@Path("/zipit/")
public class createZipFileFromRest {
	@POST
	@Path("/zip")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
	public Response returnZip(
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file")FormDataContentDisposition fileDetail) {
		File file = null; 
		saveFile(uploadedInputStream, fileDetail);
		try {
			file  = new File("./" + fileDetail.getFileName());
			file = zipIt(file, fileDetail);
			
		}catch (IOException e) {
			System.out.println(e.getMessage());
		}
		ResponseBuilder response = Response.ok(file);
		response.header("Content-Disposition","attachment; filename=" + fileDetail.getFileName().substring(0, fileDetail.getFileName().indexOf(".")+1)+"zip");
		
		return response.build();
		
		
	}
	
	private void saveFile(InputStream uploadedInputStream, FormDataContentDisposition fileDetail) {
		String location = "./" +fileDetail.getFileName();
		
		try {
			OutputStream out = new FileOutputStream (new File(location));
			int read = 0;
			byte[] bytes = new byte[1024];
			
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		}catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		
	}
	
	public File zipIt (File fileToZip, FormDataContentDisposition fileDetail) throws IOException {
        FileOutputStream fos = new FileOutputStream("./" + fileDetail.getName() +".zip");
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
        zipOut.putNextEntry(zipEntry);
        
        final byte[] bytes = new byte[1024];
        int length;
        
        while((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        zipOut.close();
        fis.close();
        fos.close();
        
        return new File("./" + fileDetail.getName() +".zip");
    }
}


