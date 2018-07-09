## Автор
Anton Melnikov (Антон Мельников)

melnikoffaa@mail.ru

## Описание
Приложение позволяет менять имена и тип телефонов, а так-же имена агентов.
<br/>
Для запуска приложения нужно положить exe файл и каталог config в одну дирректорию. 


## Сборка

* Настроить логирование **resources\log4j.properties**.<br/>

* Если приложение необходимо запускать на ПК где не установлена JAVA, то необходимо скопировать JRE в папку ./jre

* Собрать приложение:
  ````
  mvn install
  ````
 
## Настройка собранного приложения 
* Создать файл **properties.xml** в каталоге **config**: <br/>
  * Добавить адрес Сommunication Manager в тег с ключем cm_host.
  * Установить порт (по умолчанию 5022 для SSH) в тег с ключем cm_port.
  * Заполнить имя пользователя и пароль в тегах с ключами cm_user и cm_password соответственно.
  * Заполнить список агентов и телефонов в тегах с ключами agents и stations соответственно. В качестве разделителя используется запятая.
 
   
 **Пример:**<br/>
  ```xml
<?xml version="1.0" encoding="UTF-8"?>
  <!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
  <properties>
      <entry key="cm_host">5.5.5.5</entry>
      <entry key="cm_port">5022</entry>
      <entry key="cm_user">user</entry>
      <entry key="cm_password">password</entry>
      <entry key="agents">44100, 44101, 44102</entry>
      <entry key="stations">4100,4101,4102</entry>
  </properties>
  ```