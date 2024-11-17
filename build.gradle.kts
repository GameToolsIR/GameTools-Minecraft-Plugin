import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.sayandev.plugin.StickyNoteModules

plugins {
    `java-library`
    `maven-publish`
    kotlin("jvm") version "2.0.20"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("io.papermc.paperweight.userdev") version "1.7.1"
    id("org.sayandev.stickynote.project")
}

group = "ir.taher7"
version = "1.0.0-SNAPSHOT"
description = ""

repositories {
    mavenLocal()
    mavenCentral()
    //Paper
    maven("https://repo.papermc.io/repository/maven-public/")
    //Jitpack (for Vault)
    maven("https://jitpack.io")
}

dependencies {
//    implementation("com.h2database:h2:2.2.224")
//    stickynote.implementation("org.mariadb.jdbc:mariadb-java-client:3.3.3")

    paperweight.paperDevBundle("1.20.6-R0.1-SNAPSHOT")

    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("me.clip:placeholderapi:2.11.6")


    stickynote.implementation("io.socket:socket.io-client:2.1.1")
}

stickynote {
    modules(StickyNoteModules.BUKKIT)
}


tasks {
    withType<ShadowJar> {
        archiveClassifier.set(null as? String)
        archiveFileName.set("${project.name}-${version}.jar")
        destinationDirectory.set(file(rootProject.projectDir.path + "/bin"))
        from("LICENSE")
    }

    runServer {
        minecraftVersion("1.21.1")
        downloadPlugins {
            modrinth("viaversion", "5.0.3")
            modrinth("essentialsx", "2.20.1")
            hangar("placeholderapi", "2.11.6")
            url("https://download.luckperms.net/1556/bukkit/loader/LuckPerms-Bukkit-5.4.141.jar")
            url("https://github.com/MilkBowl/Vault/releases/download/1.7.3/Vault.jar")

        }
        jvmArgs("-Dnet.kyori.adventure.text.warnWhenLegacyFormattingDetected=false")
    }


    paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION

    build {
        dependsOn(shadowJar)
    }

    configurations {
        create("compileOnlyApiResolved") {
            isCanBeResolved = true
            extendsFrom(configurations.getByName("compileOnlyApi"))
        }
    }

    val publicationShadowJar by registering(com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class) {
        from(sourceSets.main.get().output)
        configurations = listOf(*configurations.toTypedArray(), project.configurations["compileOnlyApiResolved"])
        archiveClassifier.set("")
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                artifact(publicationShadowJar.get())
//                artifact(tasks["sourcesJar"])
                this.artifactId = project.name.lowercase()
                this.version = project.version as String
            }
        }

        repositories {
            maven {
                name = "sayandevelopment-repo"
                url = uri("https://repo.sayandev.org/snapshots/")

                credentials {
                    username = System.getenv("REPO_SAYAN_USER") ?: project.findProperty("repo.sayan.user") as? String
                    password = System.getenv("REPO_SAYAN_TOKEN") ?: project.findProperty("repo.sayan.token") as? String
                }
            }
        }
    }
}

java {
    disableAutoTargetJvm()
    withSourcesJar()
    if (gradle.startParameter.getTaskNames().isNotEmpty() && gradle.startParameter.getTaskNames()
            .any { it.startsWith("runServer") }
    ) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    }
}


bukkit {
    name = project.name
    version = project.version as String
    description = project.description
    website = "https://game-tools.ir"
    author = "TAHER7"

    main = "${project.group}.${project.name.lowercase()}.${project.name}"

    apiVersion = "1.13"

    softDepend = listOf(
        "Vault"
    )
}

//fun setPom(publication: MavenPublication) {
//    publication.pom {
//        name.set("melodymine")
//        description.set(rootProject.description)
//        url.set("https://github.com/vallerian/melodymine")
//        licenses {
//            license {
//                name.set("Apache License 2.0")
//                url.set("https://github.com/Vallerian/MelodyMine/blob/master/LICENSE")
//            }
//        }
//        developers {
//            developer {
//                id.set("taher7")
//                name.set("taher moradi")
//                email.set("")
//            }
//        }
//        scm {
//            connection.set("scm:git:github.com/vallerian/melodymine.git")
//            developerConnection.set("scm:git:ssh://github.com/valleryan/melodymine.git")
//            url.set("https://github.com/vallerian/melodymine/tree/master")
//        }
//    }
//}
