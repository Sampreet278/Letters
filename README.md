## Introduction
Customizing system messages in Minecraft shouldn’t be so hard, they’re just [Letters](https://github.com/Sampreet278/Letters) on the screen anyway.

Letters is a lightweight Minecraft server plugin inspired by [Lejo](https://github.com/Maksim6opka/lejo) that lets you fully customize system messages such as join, leave, death, and advancement messages with placeholder support, color formatting, and more.

---

### Download
You can download the latest compiled JAR and source code from the [Releases page](https://github.com/Sampreet278/Letters/releases). If you would like to build the project yourself, then read the [building](#building) section.

---

### Supported Events

The plugin currently supports configurable custom messages for the following events:

- [x] Player join
- [x] Player leave
- [x] Player death
- [x] Player advancement
- [x] Player chat messages
- [x] Player private messages (whispers)
- [ ] Player sleep

---

### Message Types & Priority
You can define custom messages for everyone, specific players, or permission groups. The plugin uses a clear priority order when deciding which message to show which is:

`Player-specific > Permission-based > Default`

<table>
    <thead>
        <tr>
            <th>Message Type</th>
            <th>Applies To</th>
            <th>Example</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>Default</td>
            <td>All players</td>
            <td>“%player_name% joined the server”</td>
        </tr>
        <tr>
            <td>Permission-based</td>
            <td>Only players with a certain permission (e.g., admin)</td>
            <td>"An admin, %player_name%, joined the server!"</td>
        </tr>
        <tr>
            <td>Player-specific</td>
            <td>Only the named player</td>
            <td>"The server owner, %player_name%, joined the server!"</td>
        </tr>
    </tbody>
</table>

---

### Placeholders and PAPI Support
The plugin provides a few built-in placeholders listed below. It also supports PlaceholderAPI (PAPI), allowing you to install expansion packs and use additional placeholders in your custom messages.

<table>
    <thead>
        <tr>
            <th>Placeholder</th>
            <th>Value</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>%player_name%</td>
            <td>The player’s username</td>
        </tr>
        <tr>
            <td>%sender_name%</td>
            <td>The player sending a whisper message</td>
        </tr>
        <tr>
            <td>%recipient_name%</td>
            <td>The player receiving a whisper message</td>
        </tr>
        <tr>
            <td>%whisper_message%</td>
            <td>The content of the whisper message</td>
        </tr>
        <tr>
            <td>%chat_message%</td>
            <td>The message written by the player when they send a chat message</td>
        </tr>
        <tr>
            <td>%death_message%</td>
            <td>The vanilla Minecraft death message (e.g. “Player drowned”)</td>
        </tr>
        <tr>
            <td>%advancement_name%</td>
            <td>The vanilla Minecraft advancement message name (e.g. “Isn't It Iron Pick?”)</td>
        </tr>
        <tr>
            <td>%advancement_color%</td>
            <td>The vanilla Minecraft advancement message color (e.g. <code>§a</code>)</td>
        </tr>
    </tbody>
</table>

---

### `config.yml` Structure
Each event can have multiple custom messages, and the plugin will randomly select one to send.

```yml
messages:

  default:
    join:
      - "%player_name% joined the server"
      - "%player_name% is now online"
    leave:
      - "%player_name% left the server"
      - "%player_name% went offline"
    # ... other events like death, advancement, chat, whisper can also be defined

  players:
    example_player:
      join:
        - "✨ example_player has arrived! ✨"
        - "example_player just joined the server!"
      leave:
        - "Goodbye, example_player!"
        - "example_player has left the server."
      # ... other events can be added here as well
```

---

### Formatting
You can style messages using various methods such as color codes, MiniMessage tags, hex colors, or gradients.

<table>
    <thead>
        <tr>
            <th>Format</th>
            <th>Example</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>Color Codes</td>
            <td><code>&amp;[0-9A-FK-OR]text</code></td>
        </tr>
        <tr>
            <td>Mini Message</td>
            <td><code>&lt;bold&gt;&lt;gray&gt;text&lt;/gray&gt;&lt;/bold&gt;</code></td>
        </tr>
        <tr>
            <td>Hex Codes</td>
            <td><code>&lt;color:#FF00FF&gt;text&lt;/color&gt;</code></td>
        </tr>
        <tr>
            <td>Gradients</td>
            <td><code>&lt;gradient:#FF00FF:#00FFFF&gt;text&lt;/gradient&gt;</code></td>
        </tr>
    </tbody>
</table>

---

### Building
If you would like to build the project yourself, you can do so using [Apache Maven](https://maven.apache.org/).

For building, open your terminal and go to the project directory (either extracted, or cloned) and then use the following command to compile the plugin in the `target` directory.

```bash
maven clean package
```

---

### Dependencies
Maven will download all required dependencies automatically. However, you must have JDK 21 installed and available to Maven in order to build the project.

You can install JDK 21 from [Adoptium](https://adoptium.net/temurin/releases).

---

### Licensing
This project is licensed under the [MIT License](https://github.com/Sampreet278/Letters/blob/main/LICENSE).

You are free to use, modify, distribute, and sell this software as long as the original copyright and license notice are included. For more details, see the full LICENSE file.