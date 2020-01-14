package fundamentei;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class Fundamentei {
	public static void main(String[] args) throws InterruptedException {

		String entrada = "A";
		String entrada2 = "Novo Mercado";

		if (args.length == 0 || args[0] == null) {
			System.out.println("Parametro de entrada: S = Stocks, A = Ações");
			return;
		}

		entrada = (args[0].equals("S") ? "AMERICAN_COMPANY"
				: "BRAZILIAN_COMPANY");

		entrada2 = (args[1] != null && args[1].equals("O") ? "O" : entrada2);

		System.setProperty("webdriver.chrome.driver",
				"G:\\eclipse-workspace\\fundamentei\\src\\main\\resources\\webdriver\\chromedriver.exe");

		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("--no-sandbox");
		chromeOptions.addArguments("--headless");
		chromeOptions.addArguments("disable-gpu");

		WebDriver driver = new ChromeDriver(chromeOptions);

		// WebDriverWait wait = new WebDriverWait(driver, 20);
		// wait.until(ExpectedConditions.elementToBeClickable(lastElementToLoad));

		driver.get("https://fundamentei.com/login");
		WebElement username = driver.findElement(By.name("email"));
		WebElement password = driver.findElement(By.name("password"));
		WebElement login = driver
				.findElement(By.xpath("//button[text()='Continuar']"));
		username.sendKeys("eduardo.beninca@gmail.com");
		password.sendKeys("6b7fhlet");
		login.click();

		Thread.sleep(2000);

		String expectedUrl = "https://fundamentei.com/";
		String actualUrl = driver.getCurrentUrl();
		System.out.println(actualUrl);
		if (!expectedUrl.equalsIgnoreCase(actualUrl)) {
			System.out.print("Pagina inesperada p�s login " + actualUrl);
			return;
		}

		System.out.println("SELECT...." + driver
				.findElement(By.xpath("//select[@class='css-byl8eb']")));
		Select dropdown = new Select(
				driver.findElement(By.xpath("//select[@class='css-byl8eb']")));
		dropdown.selectByValue(entrada);
		Thread.sleep(2000);

		if (entrada.equals("BRAZILIAN_COMPANY")) {

			if (entrada2.equals("O")) {
				
				WebElement tagAlong = driver.findElement(By.xpath(
						"//button[@aria-label='Tag Along mínimo das Ações Ordinárias']"));
				Actions moveTagAlong = new Actions(driver);
				moveTagAlong.click(tagAlong).build().perform();
				Thread.sleep(1000);
				for (int i = 0; i < 100; i++) {
					moveTagAlong.sendKeys(Keys.ARROW_RIGHT).build().perform();
					Thread.sleep(200);
				}

				WebElement freeFloat = driver.findElement(By.xpath(
						"//button[@aria-label='Free Float mínimo das Ações Ordinárias']"));
				Actions moveFreeFloat = new Actions(driver);
				moveFreeFloat.click(freeFloat).build().perform();
				Thread.sleep(1000);
				for (int i = 0; i < 10; i++) {
					moveFreeFloat.sendKeys(Keys.ARROW_RIGHT).build().perform();
					Thread.sleep(200);
				}

			} else {
				WebElement novoMercado = driver.findElement(
						By.xpath("//button[text()='Novo Mercado']"));
				novoMercado.click();
				Thread.sleep(2000);
			}
		}

		System.out.println("Ativos...." + driver
				.findElement(By.xpath("//strong[@class='css-1vg6q84']")));
		WebElement ativos = driver
				.findElement(By.xpath("//strong[@class='css-1vg6q84']"));

		// verificando o numero de cliques necessarios
		List<WebElement> acoesPrimeiraPag = driver
				.findElements(By.xpath("//a[@href]"));
		System.out.println(acoesPrimeiraPag.size());
		double cliquesVermais = Integer.parseInt(ativos.getText())
				/ acoesPrimeiraPag.size();
		System.out.println("Numero de Cliques >>> " + cliquesVermais);

		// realizando cliques
		for (int i = 0; i <= cliquesVermais; i++) {
			try {
				WebElement verMais = driver.findElement(
						By.xpath("//button[text()='Ver mais ativos']"));
				verMais.click();
				Thread.sleep(1000);
			} catch (NoSuchElementException ex) {
				System.out.println(ex.getMessage());
				break;
			}
		}

		// listando os elementos
		List<WebElement> acoesTotais = driver
				.findElements(By.xpath("//a[@class='css-1kaq1tm']"));
		List<String> acoesHref = new ArrayList<String>();
		System.out.println("Total de Ações: " + acoesTotais.size());
		for (WebElement webElement : acoesTotais) {
			acoesHref.add(webElement.getAttribute("href"));
		}
		System.out.println("Acoes HREF: " + acoesHref.size());

		List<Acao> listaAcoes = new ArrayList<Acao>();
		// acessando acoes

		for (String href : acoesHref) {
			System.out.println("endereço ação: " + href);
			driver.get(href);

			actualUrl = driver.getCurrentUrl();
			if (!href.equalsIgnoreCase(actualUrl)) {
				System.out.print("Pagina inesperada " + href + ", deveria ser: "
						+ actualUrl);
				continue;
			}

			try {
				WebElement nome;
				WebElement codigo;

				if (entrada.equals("BRAZILIAN_COMPANY")) {
					nome = driver.findElement(
							By.xpath("//span[@class='css-1olkw19']"));
					codigo = driver.findElement(
							By.xpath("//span[@class='css-lwqnf3']"));
				} else {
					nome = driver.findElement(
							By.xpath("//h1[@class='css-16bz76r']"));
					codigo = driver
							.findElement(By.xpath("//h3[@class='css-2q74sx']"));
				}
				WebElement nota = driver
						.findElement(By.xpath("//strong[@class='css-v0mgew']"));

				Acao acao = new Acao();
				acao.setNome(nome.getText());
				acao.setCodigo(codigo.getText());
				acao.setNota(Double.valueOf(nota.getText().split(" ")[0]));
				listaAcoes.add(acao);

			} catch (NoSuchElementException ex) {
				System.out.println(ex.getMessage());
			}
		}

		Collections.sort(listaAcoes);
		Collections.reverse(listaAcoes);
		System.out.println(listaAcoes.size());

		File file = new File("logs/rankingAcoes_"
				+ LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
				+ entrada + "-" + entrada2 + ".csv");

		try {
			FileWriter fw = new FileWriter(file);
			for (Acao acao : listaAcoes) {
				fw.write(acao.toCSV());
				fw.write(System.lineSeparator());
			}
			fw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}