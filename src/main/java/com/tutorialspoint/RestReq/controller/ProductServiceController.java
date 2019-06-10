package com.tutorialspoint.RestReq.controller;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.tutorialspoint.RestReq.exception.ProductNotfoundException;
import com.tutorialspoint.RestReq.model.Product;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


@RestController
public class ProductServiceController {
	
	private static Map<String, Product> productRepo = new HashMap<>();
	
	@Autowired
	RestTemplate restTemplate;
	
	static {
		
		 Product honey= new Product();
		 honey.setId("1");
		 honey.setName("Honey");
		 productRepo.put(honey.getId(), honey);
		 
		 Product almond = new Product();
		 almond.setId("2");
		 almond.setName("Almond");
		 productRepo.put(almond.getId(),almond);		 
	}
	
	@RequestMapping(value="/products/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Object> delete (@PathVariable("id") String id){
		if(!productRepo.containsKey(id))
			throw new ProductNotfoundException();

		productRepo.remove(id);
		return new ResponseEntity<>("Product is deleted successfully",HttpStatus.OK);
	}
	
	@RequestMapping(value="/products/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Object> updateProduct(@PathVariable("id") String id,@RequestBody Product product){
		if(!productRepo.containsKey(id))
			throw new ProductNotfoundException();
		
		productRepo.remove(id);
		product.setId(id);
		productRepo.put(id,product);		
		return new ResponseEntity<>("Product is updated successfully",HttpStatus.OK);
	}
	
	@RequestMapping(value="/products", method=RequestMethod.POST)
	public ResponseEntity<Object> createProduct(@RequestBody Product product)
	{
		productRepo.put(product.getId(), product);
		return new ResponseEntity<> ("Product is created successfully" , HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/products")
	public ResponseEntity<Object> getProduct(){
		return new ResponseEntity<>(productRepo.values(),HttpStatus.OK);
	}
	
	@RequestMapping(value = "/template/products")
	public String getProductList(){
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		return restTemplate.exchange("http://localhost:8080/products", HttpMethod.GET,entity,String.class).getBody();
	}
	
	@RequestMapping(value="/template/products", method = RequestMethod.POST)
	public String createProducts(@RequestBody Product product){		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<Product> entity = new HttpEntity<Product>(product,headers);
		return restTemplate.exchange("http://localhost:8080/products", HttpMethod.POST,entity,String.class).getBody();		
		
	}
	
	@RequestMapping(value= "/template/products/{id}" , method = RequestMethod.PUT)
	public String updateProduct2(@PathVariable("id")  String id, @RequestBody Product product)
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<Product> entity = new HttpEntity<Product>(product,headers);
		return restTemplate.exchange("http://localhost:8080/products/"+id,HttpMethod.PUT,entity,String.class).getBody();
		
	}
	
	@RequestMapping(value="/template/products/{id}",method=RequestMethod.DELETE)
	public String deleteProduct2(@PathVariable("id") String id){
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<Product> entity = new HttpEntity<Product>(headers);
		return restTemplate.exchange("http://localhost:8080/products/"+id, HttpMethod.DELETE,entity,String.class).getBody();
	}
	

	@RequestMapping(value="/upload" ,  method = RequestMethod.POST,consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public String fileUpload(@RequestParam("file") MultipartFile file) throws IOException{
		
		File convertFile = new File ("c:/fileupload/"+file.getOriginalFilename());
		convertFile.createNewFile();
		FileOutputStream fout = new FileOutputStream(convertFile);
		fout.write(file.getBytes());
		fout.close();
		return "File is upload successfully";		
		
	}
	
	@RequestMapping(value="/download" , method = RequestMethod.GET)
	public ResponseEntity<Object> downloadFile() throws IOException {
		String filename = "C:\\csb.log";
		File file = new File (filename);
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", String.format("attachment;filename=\"%s\"", file.getName()));
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		
		ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(MediaType.parseMediaType("application/txt")).body(resource);
		
		return responseEntity;
	}
}
