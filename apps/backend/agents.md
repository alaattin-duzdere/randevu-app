# Randevu-App: Yapay Zeka Ajanları İçin Geliştirici Rehberi

Bu belge, **Randevu-App** projesine katkıda bulunacak Yapay Zeka (AI) ajanları ve geliştiriciler için temel rehberdir. Projenin mimarisini, teknoloji yığınını, geliştirme standartlarını ve operasyonel kurallarını özetler. Lütfen kod yazmadan veya değişiklik yapmadan önce bu kuralları dikkate alın.

## 1. Proje Özeti
- **Proje Adı**: Randevu-App
- **Amacı**: Randevu, personel ve kaynak yönetimini sağlayan güvenilir, ölçeklenebilir ve modern bir platform.
- **Teknoloji Yığını**:
  - **Dil**: Java 25
  - **Framework**: Spring Boot 4.x
  - **Veritabanı**: PostgreSQL
  - **Kimlik Doğrulama**: `auth-core-spring-boot-starter` (Özel kütüphane)
  - **Diğer Teknolojiler**: Lombok, MapStruct, Springdoc OpenAPI (Swagger), Redis, Thymeleaf, Spring Mail.
- **Mimari Yaklaşım**: Domain-Driven Design (DDD) ve Layered Architecture (Katmanlı Mimari).

## 2. Dizin ve Mimari Yapısı (`src/main/java/RandevuApp`)
Uygulama, sorumlulukların net bir şekilde ayrıldığı Domain-Driven Design prensiplerine göre klasörlenmiştir:

- **`api/`**: RESTful Controller'lar ve API uç noktaları. Dış dünyaya açılan kapıdır. İş mantığı içermemelidir.
- **`domain/`**: İş mantığının kalbi. Entity'ler, Aggregate Root'lar, iş kuralları, DTO'lar (örn. Request/Response modelleri) ve Repository arayüzleri burada bulunur.
- **`infrastructure/`**: Altyapı kodları. Veritabanı işlemleri (JPA/Hibernate), Redis önbellek mekanizmaları, veritabanı konfigürasyonları ve dış servis implementasyonları.
- **`config/`**: Spring Boot konfigürasyon sınıfları (Security, Swagger, Redis vb.). Multi-profile (çoklu ortam) yapılandırmalarını içerir.
- **`commons/`**: Proje genelinde kullanılan ortak modeller, sabitler (constants) ve enumlar.
- **`exceptions/`**: Global hata yönetimi (`GlobalExceptionHandler`) ve uygulamaya özel hata sınıfları (Custom Exceptions).
- **`integration/`**: 3. parti sistemlerle veya diğer servislerle olan entegrasyonlar.
- **`security/`**: JWT doğrulama, yetkilendirme, rol bazlı erişim kontrolleri ve güvenlik filtreleri.
- **`utils/`**: Yardımcı (Utility) metotlar ve sınıflar (Örn: Tarih çeviriciler, string manipülasyonları).
- **`test/`**: Test yardımcı sınıfları, mock'lar ve test konfigürasyonları.

## 3. Yapay Zeka Ajanı Görev ve Protokolleri
Ajanlar, projenin geliştirilmesi, refactor edilmesi ve bakımında aşağıdaki kurallara kesinlikle uymalıdır:

- **Öncelikli Kural**: Kod kalitesini, mimari bütünlüğü ve "Clean Code" standartlarını her zaman koruyun.
- **Dosya Değiştirme Protokolü**:
  - Kesinlikle IDE'nin sağladığı veya sisteme entegre araçları (örn. `write_to_file`, `replace_file_content`, `multi_replace_file_content`) kullanın.
  - Bash CLI üzerinden `sed`, `awk` gibi komutlarla dosyaları doğrudan düzenlemeye **çalışmayın**. Bu durum dosya senkronizasyonunu bozabilir.
  - Herhangi bir sınıfı değiştirmeden önce `view_file` ile içeriğini okuyun.
- **Güvenlik ve Çevre Değişkenleri**: Parola, API key veya token gibi hassas verileri asla koda hardcode (sabit) olarak gömmeyin. Mutlaka `.env` veya Spring Boot `application-{profile}.properties` altyapısını kullanın.

## 4. Geliştirme İş Akışı (Workflow)
1. **Analiz Et**: Kullanıcının isteğini (yeni özellik, hata düzeltme, refactor) dikkatlice oku ve gereksinimleri anla.
2. **Keşfet**: Bağlamı anlamak için `list_dir` ve `view_file` araçlarıyla ilgili `api`, `domain` ve `infrastructure` dosyalarını incele.
3. **Planla**: Değişiklikleri uygulamadan önce neyin nerede değişeceğini zihninde tasarla. Köklü bir değişiklikse (örn. veritabanı şema değişimi, yeni bir modül), önce kullanıcıdan onay al veya plan oluştur.
4. **Geliştir**: 
   - İlgili DTO'yu oluştur.
   - Domain Entity'yi güncelle.
   - Repository arayüzünü tanımla.
   - Infrastructure'da uygula.
   - API katmanına entegre et.
5. **Doğrula**: Değişiklikler tamamlandığında syntax'in doğru olduğundan ve eksik bir import/metot kalmadığından emin ol.

## 5. Kodlama Standartları ve Pratikleri
- **Modern Java Kullanımı**: Java 25 özelliklerinden (Records, Pattern Matching, Sealed Classes, Streams, Optionals) faydalanın.
- **DDD Kuralları**: İş mantığını servislerde veya controller'larda değil, olabildiğince domain objelerinin (Entity/Aggregate) içinde kapsülleyin.
- **Bağımlılık Enjeksiyonu (DI)**: Field injection (`@Autowired`) yerine her zaman **Constructor Injection** kullanın. Lombok `@RequiredArgsConstructor` bu iş için idealdir.
- **Immutability (Değişmezlik)**: Özellikle DTO'lar ve Value Object'ler için immutable veri yapılarını (örn. Java `record`) tercih edin.
- **Validasyon**: Controller seviyesinde JSR 380 (Bean Validation) anotasyonlarını (`@Valid`, `@NotNull`, `@NotBlank`) Request DTO'ları üzerinde mutlaka kullanın.
- **Dökümantasyon**: 
  - Yeni eklenen Controller metotlarına (API uç noktalarına) Springdoc `@Operation` ve `@ApiResponse` anotasyonlarını ekleyin.
  - Karmaşık veya kritik iş mantığı barındıran metotlara açıklayıcı Javadoc yorumları yazın.

---
*Bu doküman, Randevu-App projesinin mimarisi ve iş akışları geliştikçe güncellenmesi gereken yaşayan bir rehberdir.*