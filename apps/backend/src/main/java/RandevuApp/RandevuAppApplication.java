package RandevuApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories
@EnableAsync
@EnableScheduling
public class RandevuAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(RandevuAppApplication.class, args);
	}

	// Nerede kalmıştık ?
	// Talos-Gym projesindeki çözülen sorunlar buraya da aktarılıyor;
	// Appointment domain servisteki status ile ilgili metodlarla ilgilen , zaman ayır.


	// Notlar:
	// + ( tamamlandı ) Loginde access token oluşturulurken herhangi bir claim eklenmiyor.Belki jit gibi bir veya fazla claim eklenebilir.
	// + ( tamamlandı ) User paketinde email ve telefon bilgilerinin de değiştirilebilmesi gerekli
	// + ( tamamlandı ) REGISTRATION purposeuna sahip verificationlarda kullanıcının channel ve type secme hakkı olmamalıdırkullanıcının channel ve type secme hakkı olmamalıdır. Suanki sistemde var
	// + ( tamamlandı ) deleteUser metodunda aktif randevu ve business kaydı olup olmadığı kontrol edilmeli
	// + ( tamamlandı ) Phone için bir validasyon annotation yazılacak
	// + ( tamamlandı ) domain servislerde bazı metodlarda dtolar kullanılıyor.!!
	// - SpamProtectionFilter ve TokenBlacklistService sınıflarında redis kullanılıyor. Redis kullanımı şuanda dağıtık olarak sağlanıyor.
	// + UserControllerdaki void/string dönüşler için ResponseEntity.noContent kullanmayı düşün. Burada bir karar verilmesi lazım, eğer frontendden belirli bir standartta cevap bekleniyor mu (VustomResponseBody)
	// + Bütün dtolar validation annotationlar kullanacak
	// - Email/Phone yani kısaca phoneNumber için özel bir validasyon yazılabilir.
	// - Şuna karar verilmeli; authentication controllerda parametre olarak mı gelmeli yoksa SecurityContextHolder'dan mı çekilmeli. Şuan da her iki yöntem de uygulanıyor
	// - delete business metodunda eğer aktif randevu varsa silme işlemini engellemek yerine randevular iptal ettirilip(canceled stat) kullanıcı bilgilendirilebilir.
	// - BusinessServiceImpl sınıfındaki appointment repository bağımlılığından kurtul
	// - ServiceOfferingServiceImpl deleteService metodunda bu servisi kullanan aktif randevu var mı kontrol edilecek
	// - Domain servislerdeki performUpdate metodlarının gerekliliğini gözden geçir.

	// Feat:
	// + Adresler için format belirlenecek ve gerekli val7idasyonlar yazılacak
	// - İşletmeler için yorumlar da eklenecek
	// - Ödeme apisi bağlanacak
	// - Business yetkileri yönetilecek. Alınan ödemeye userın hangi businessları işletebilme yetkileri olduğuna karar verilecek. Güzel bir rol/ yetki yönetimine ihtiyaç var.
	// - Admin paneli, dashboard gibi yapılar için gerekli endpointlerin varlığına bakılacak.
	// - Genel bir spam protection filter olması gerekli.
	// - Bütün dönütler tek bir dosyadan çekilebilir, mesaj stringlerinin kodda olması kötü.
}
