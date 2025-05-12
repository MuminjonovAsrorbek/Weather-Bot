# Sky Info Bot

Bu Telegram bot foydalanuvchilarga ob-havo ma'lumotlarini taqdim etuvchi loyiha. Bot orqali joriy ob-havo, soatlik ma'lumotlar va astronomik ma'lumotlarni olish mumkin.

## Texnologiyalar

Loyihada quyidagi texnologiyalar va kutubxonalar ishlatilgan:

- **Java 22** - Asosiy dasturlash tili
- **Maven** - Loyiha boshqarish va build qilish vositasi
- **Telegram Bot API** - Telegram bot yaratish uchun Java kutubxonasi
- **WeatherAPI.com** - Ob-havo ma'lumotlarini olish uchun API
- **Lombok** - Kod hajmini kamaytirish uchun annotatsiyalar
- **SLF4J** - Logging uchun fasad
- **Gson** - JSON ma'lumotlarini qayta ishlash uchun kutubxona
- **java-dotenv** - Environment o'zgaruvchilarini boshqarish uchun kutubxona

## Imkoniyatlar

- Joriy ob-havo ma'lumotlarini ko'rish
- Soatlik ob-havo ma'lumotlarini ko'rish
- Astronomik ma'lumotlarni ko'rish (quyosh chiqishi/botishi, oy fazalari)
- Turli shaharlar uchun ob-havo ma'lumotlarini olish
- Lokatsiya orqali ob-havo ma'lumotlarini olish
- Ko'p tilli interfeys (O'zbek, Ingliz, Rus)

## O'rnatish va Ishga tushirish

1. Loyihani clone qiling:
```bash
git clone https://github.com/username/sky-info-bot.git
cd sky-info-bot
```

2. `.env` faylini yarating va quyidagi ma'lumotlarni kiriting:
```
API_TOKEN = "https://api.weatherapi.com/v1/"
API_KEY = "weatherapi.com saytidan olingan API kalit"
BOT_TOKEN = "BotFather orqali olingan Telegram bot tokeni"
BOT_USERNAME = "Botingiz usernamesi"
```

3. Maven orqali loyihani build qiling:
```bash
mvn clean package
```

4. Loyihani ishga tushiring:
```bash
java -jar target/sky-info-bot-1.0-SNAPSHOT.jar
```

## API kalitlarini olish

### Telegram Bot Token
1. Telegram'da [@BotFather](https://t.me/BotFather) ga murojaat qiling
2. `/newbot` buyrug'ini yuboring va ko'rsatmalarga amal qiling
3. Bot yaratilgandan so'ng, sizga API token beriladi

### WeatherAPI.com API kaliti
1. [WeatherAPI.com](https://www.weatherapi.com/) saytiga kiring
2. Ro'yxatdan o'ting va bepul API kalitini oling

## Loyiha strukturasi

- `src/main/java/uz/dev/` - Asosiy kod joylashgan katalog
  - `actions/` - API bilan ishlash uchun action klasslari
  - `connect/` - HTTP so'rovlarni yuborish uchun klasslari
  - `dto/` - Ma'lumotlarni uzatish obyektlari
  - `model/` - Loyiha modellari
  - `process/` - Xabarlarni qayta ishlash logikasi
  - `service/` - Asosiy servis klasslari
  - `utils/` - Yordamchi utilita klasslari

## Litsenziya

Bu loyiha [MIT litsenziyasi](LICENSE) ostida tarqatiladi.