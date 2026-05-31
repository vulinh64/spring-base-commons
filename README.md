## Introduction to the Project

This is the support project for [spring-base-auth](https://github.com/vulinh64/spring-base-auth), [spring-base](https://github.com/vulinh64/spring-base) and [spring-base-event](https://github.com/vulinh64/spring-base-event) repositories. It provides shared resources that are utilized by both projects to ensure consistency and reduce redundancy.

However, it will introduce some mental overheads for new developers. Consult each repository's README for more details on how to use this project effectively. For the time being, this project is served as a demonstration on how most developers will work in a corporation with dedicated Maven repositories for shared libraries.

> [!WARNING]
> 
> Well, except that this repository is public, and you are to build the dependencies locally.

The repository is located [here](https://github.com/vulinh64/spring-base-commons).

## Local Development

When working on this library locally, install it into your local Maven repository before running the dependent projects:

- **Windows:** run `.\mvnw.cmd clean install`

- **Linux / macOS:** run `./mvnw clean install`

This publishes the current `spring-base-commons` artifact version to your local `.m2` repository, allowing sibling projects such as `spring-base-auth`, `spring-base`, and `spring-base-event` to resolve it.

This project also uses Git tags for released versions. To install a specific released version locally, check out the matching tag first, then run `clean install`:

```bash
git checkout <version-tag>
./mvnw clean install
```

On Windows, use `.\mvnw.cmd clean install` instead.

Each installed version is stored separately in `.m2` by Maven coordinates, so installing one tagged version does not overwrite other versions.

## Versioning

For now, this project is using manual versioning. To specify the version:

- Update the version in the `pom.xml` file accordingly, specially at the section `<version>`.

- Commit and push the changes.

- Create a new tag with the version number (either on GitHub or using Git commands).

|                ![pepe-sad](pepe-sad.png)                |
|:-------------------------------------------------------:|
| ~~Here is your pepe-sad that wastes some bandwidth xD~~ |

~~Why am I wasting my time doing this manually?~~
