# Akka Remote

Exemple de programme en Java pour créer et utiliser des Acteurs Akka répartis sur plusieurs JVM.

Pour compiler le code, utiliser Maven avec les commandes suivantes :

```
mvn clean compile install
```

Lancer d'abord la banque :

```
mvn exec:java -pl bank -Dexec.mainClass="com.perfect.bank.App"
```

Lancer ensuite des banquiers :

```
mvn exec:java -pl banker -Dexec.mainClass="com.perfect.banker.App"
```

Lancer enfin des clients :

```
mvn exec:java -pl client -Dexec.mainClass="com.perfect.bank.App"
```
