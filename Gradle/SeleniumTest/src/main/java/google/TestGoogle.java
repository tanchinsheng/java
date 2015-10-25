/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package google;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 *
 * @author cstan
 */
public class TestGoogle {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "D:\\GitHub\\java\\Gradle\\SeleniumTest\\exe\\chromedriver.exe");

        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("http://bbs.sgcn.com/member.php?mod=logging&action=login");
        Thread.sleep(5000);  // Let the user actually see something!
        //WebElement searchBox = driver.findElement(By.name("q"));
        //searchBox.sendKeys("ChromeDriver");
        //searchBox.submit();

        driver.findElement(By.id("u_u")).sendKeys("江西省人");
        driver.findElement(By.id("u_p")).sendKeys("diesel");
        driver.findElement(By.className("u_s1")).submit();
        Thread.sleep(5000);
        driver.findElement(By.className("bind_skip")).submit();

        driver.get("http://bbs.sgcn.com/thread-15716879-1-1.html");
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        WebElement we = driver.findElement(By.id("fastpostsubmit"));
        jse.executeScript("arguments[0].scrollIntoView(true);", we);
        driver.findElement(By.id("fastpostsubmit")).click();
        Thread.sleep(5000);  // Let the user actually see something!
        driver.quit();
    }

}
