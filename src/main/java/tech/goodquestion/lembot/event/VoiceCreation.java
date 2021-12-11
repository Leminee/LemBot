package tech.goodquestion.lembot.event;

import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class VoiceCreation extends ListenerAdapter {

       public static final String[] LEFT = {
                "admiring",
                "adoring",
                "affectionate",
                "agitated",
                "amazing",
                "angry",
                "awesome",
                "beautiful",
                "blissful",
                "bold",
                "boring",
                "brave",
                "busy",
                "charming",
                "clever",
                "cool",
                "compassionate",
                "competent",
                "condescending",
                "confident",
                "crazy",
                "dazzling",
                "determined",
                "distracted",
                "dreamy",
                "eager",
                "elastic",
                "elegant",
                "eloquent",
                "epic",
                "exciting",
                "festive",
                "friendly",
                "frosty",
                "funny",
                "gallant",
                "gracious",
                "great",
                "happy",
                "hardcore",
                "hopeful",
                "hungry",
                "infallible",
                "inspiring",
                "interesting",
                "intelligent",
                "kind",
                "laughing",
                "loving",
                "lucid",
                "magical",
                "modest",
                "musing",
                "naughty",
                "nervous",
                "nice",
                "nifty",
                "nostalgic",
                "objective",
                "optimistic",
                "peaceful",
                "priceless",
                "relaxed",
                "romantic",
                "sad",
                "sleepy",
                "strange",
                "stupefied",
                "suspicious",
                "sweet",
                "thirsty",
                "trusting",
                "vibrant",
                "vigilant",
                "wonderful",
                "zen",
        };

        public static final String[] RIGHT = {

                // Muhammad ibn Jābir al-Ḥarrānī al-Battānī was a founding father of astronomy. https://en.wikipedia.org/wiki/Mu%E1%B8%A5ammad_ibn_J%C4%81bir_al-%E1%B8%A4arr%C4%81n%C4%AB_al-Batt%C4%81n%C4%AB
                "albattani",
                // Frances E. Allen, became the first female IBM Fellow in 1989. In 2006, she became the first female recipient of the ACM's Turing Award. https://en.wikipedia.org/wiki/Frances_E._Allen
                "allen",
                // Kathleen Antonelli, American computer programmer and one of the six original programmers of the ENIAC - https://en.wikipedia.org/wiki/Kathleen_Antonelli
                "antonelli",
                // Charles Babbage invented the concept of a programmable computer. https://en.wikipedia.org/wiki/Charles_Babbage.
                "babbage",
                // Kathleen Booth, she's credited with writing the first assembly language. https://en.wikipedia.org/wiki/Kathleen_Booth
                "booth",
                // Katherine Louise Bouman is an imaging scientist and Assistant Professor of Computer Science at the California Institute of Technology. She researches computational methods for imaging, and developed an algorithm that made possible the picture first visualization of a black hole using the Event Horizon Telescope. - https://en.wikipedia.org/wiki/Katie_Bouman
                "bouman",
                // Vinton Gray Cerf - American Internet pioneer, recognised as one of "the fathers of the Internet". With Robert Elliot Kahn, he designed TCP and IP, the primary data communication protocols of the Internet and other computer networks. https://en.wikipedia.org/wiki/Vint_Cerf
                "cerf",
                // David Lee Chaum - American computer scientist and cryptographer. Known for his seminal contributions in the field of anonymous communication. https://en.wikipedia.org/wiki/David_Chaum
                "chaum",
                // Bram Cohen - American computer programmer and author of the BitTorrent peer-to-peer protocol. https://en.wikipedia.org/wiki/Bram_Cohen
                "cohen",
                // Seymour Roger Cray was an American electrical engineer and supercomputer architect who designed a series of computers that were the fastest in the world for decades. https://en.wikipedia.org/wiki/Seymour_Cray
                "cray",
                // A. K. (Alexander Keewatin) Dewdney, Canadian mathematician, computer scientist, author and filmmaker. Contributor to Scientific American's "Computer Recreations" from 1984 to 1991. Author of Core War (program), The Planiverse, The Armchair Universe, The Magic Machine, The New Turing Omnibus, and more. https://en.wikipedia.org/wiki/Alexander_Dewdney
                "dewdney",
                // Bailey Whitfield Diffie - American cryptographer and one of the pioneers of public-key cryptography. https://en.wikipedia.org/wiki/Whitfield_Diffie
                "diffie",
                // Edsger Wybe Dijkstra was a Dutch computer scientist and mathematical scientist. https://en.wikipedia.org/wiki/Edsger_W._Dijkstra.
                "dijkstra",
                // Annie Easley - She was a leading member of the team which developed software for the Centaur rocket stage and one of the first African-Americans in her field. https://en.wikipedia.org/wiki/Annie_Easley
                "easley",
                // Alexandra Asanovna Elbakyan (Russian: Алекса́ндра Аса́новна Элбакя́н) is a Kazakhstani graduate student, computer programmer, internet pirate in hiding, and the creator of the site Sci-Hub. Nature has listed her in 2016 in the top ten people that mattered in science, and Ars Technica has compared her to Aaron Swartz. - https://en.wikipedia.org/wiki/Alexandra_Elbakyan
                "elbakyan",
                // Taher A. ElGamal - Egyptian cryptographer best known for the ElGamal discrete log cryptosystem and the ElGamal digital signature scheme. https://en.wikipedia.org/wiki/Taher_Elgamal
                "elgamal",
                // James Henry Ellis - British engineer and cryptographer employed by the GCHQ. Best known for conceiving for the first time, the idea of public-key cryptography. https://en.wikipedia.org/wiki/James_H._Ellis
                "ellis",
                // Horst Feistel - German-born American cryptographer who was one of the earliest non-government researchers to study the design and theory of block ciphers. Co-developer of DES and Lucifer. Feistel networks, a symmetric structure used in the construction of block ciphers are named after him. https://en.wikipedia.org/wiki/Horst_Feistel
                "feistel",
                // William Henry "Bill" Gates III is an American business magnate, philanthropist, investor, computer programmer, and inventor. https://en.wikipedia.org/wiki/Bill_Gates
                "gates",
                // Adele Goldberg, was one of the designers and developers of the Smalltalk language. https://en.wikipedia.org/wiki/Adele_Goldberg_(computer_scientist)
                "goldberg",
                // Adele Goldstine, born Adele Katz, wrote the complete technical description for the first electronic digital computer, ENIAC. https://en.wikipedia.org/wiki/Adele_Goldstine
                "goldstine",
                // Shafi Goldwasser is a computer scientist known for creating theoretical foundations of modern cryptography. Winner of 2012 ACM Turing Award. https://en.wikipedia.org/wiki/Shafi_Goldwasser
                "goldwasser",
                // Lois Haibt - American computer scientist, part of the team at IBM that developed FORTRAN - https://en.wikipedia.org/wiki/Lois_Haibt
                "haibt",
                // Martin Edward Hellman - American cryptologist, best known for his invention of public-key cryptography in co-operation with Whitfield Diffie and Ralph Merkle. https://en.wikipedia.org/wiki/Martin_Hellman
                "hellman",
                // Erna Schneider Hoover revolutionized modern communication by inventing a computerized telephone switching method. https://en.wikipedia.org/wiki/Erna_Schneider_Hoover
                "hoover",
                // Grace Hopper developed the first compiler for a computer programming language and  is credited with popularizing the term "debugging" for fixing computer glitches. https://en.wikipedia.org/wiki/Grace_Hopper
                "hopper",
                // Betty Jennings - one of the original programmers of the ENIAC. https://en.wikipedia.org/wiki/ENIAC - https://en.wikipedia.org/wiki/Jean_Bartik
                "jennings",
                // Mary Lou Jepsen, was the founder and chief technology officer of One Laptop Per Child (OLPC), and the founder of Pixel Qi. https://en.wikipedia.org/wiki/Mary_Lou_Jepsen
                "jepsen",
                // Susan Kare, created the icons and many of the interface elements for the original Apple Macintosh in the 1980s, and was an original employee of NeXT, working as the Creative Director. https://en.wikipedia.org/wiki/Susan_Kare
                "kare",
                // Mary Kenneth Keller, Sister Mary Kenneth Keller became the first American woman to earn a PhD in Computer Science in 1965. https://en.wikipedia.org/wiki/Mary_Kenneth_Keller
                "keller",
                // Jack Kilby invented silicon integrated circuits and gave Silicon Valley its name. - https://en.wikipedia.org/wiki/Jack_Kilby
                "kilby",
                // Donald Knuth - American computer scientist, author of "The Art of Computer Programming" and creator of the TeX typesetting system. https://en.wikipedia.org/wiki/Donald_Knuth
                "knuth",
                // Hedy Lamarr - Actress and inventor. The principles of her work are now incorporated into modern Wi-Fi, CDMA and Bluetooth technology. https://en.wikipedia.org/wiki/Hedy_Lamarr
                "lamarr",
                // Leslie B. Lamport - American computer scientist. Lamport is best known for his seminal work in distributed systems and was the winner of the 2013 Turing Award. https://en.wikipedia.org/wiki/Leslie_Lamport
                "lamport",
                // Ruth Lichterman - one of the original programmers of the ENIAC. https://en.wikipedia.org/wiki/ENIAC - https://en.wikipedia.org/wiki/Ruth_Teitelbaum
                "lichterman",
                // Barbara Liskov - co-developed the Liskov substitution principle. Liskov was also the winner of the Turing Prize in 2008. - https://en.wikipedia.org/wiki/Barbara_Liskov
                "liskov",
                // Ada Lovelace invented the first algorithm. https://en.wikipedia.org/wiki/Ada_Lovelace (thanks James Turnbull)
                "lovelace",
                // Yukihiro Matsumoto - Japanese computer scientist and software programmer best known as the chief designer of the Ruby programming language. https://en.wikipedia.org/wiki/Yukihiro_Matsumoto
                "matsumoto",
                // John McCarthy invented LISP: https://en.wikipedia.org/wiki/John_McCarthy_(computer_scientist)
                "mccarthy",
                // Malcolm McLean invented the modern shipping container: https://en.wikipedia.org/wiki/Malcom_McLean
                "mclean",
                // Kay McNulty - one of the original programmers of the ENIAC. https://en.wikipedia.org/wiki/ENIAC - https://en.wikipedia.org/wiki/Kathleen_Antonelli
                "mcnulty",
                // Carla Meninsky, was the game designer and programmer for Atari 2600 games Dodge 'Em and Warlords. https://en.wikipedia.org/wiki/Carla_Meninsky
                "meninsky",
                // Ralph C. Merkle - American computer scientist, known for devising Merkle's puzzles - one of the very first schemes for public-key cryptography. Also, inventor of Merkle trees and co-inventor of the Merkle-Damgård construction for building collision-resistant cryptographic hash functions and the Merkle-Hellman knapsack cryptosystem. https://en.wikipedia.org/wiki/Ralph_Merkle
                "merkle",
                // Samuel Morse - contributed to the invention of a single-wire telegraph system based on European telegraphs and was a co-developer of the Morse code - https://en.wikipedia.org/wiki/Samuel_Morse
                "morse",
                // Ian Murdock - founder of the Debian project - https://en.wikipedia.org/wiki/Ian_Murdock
                "murdock",
                // John von Neumann - todays computer architectures are based on the von Neumann architecture. https://en.wikipedia.org/wiki/Von_Neumann_architecture
                "neumann",
                // Isaac Newton invented classic mechanics and modern optics. https://en.wikipedia.org/wiki/Isaac_Newton
                "newton",
                // Florence Nightingale, more prominently known as a nurse, was also the first female member of the Royal Statistical Society and a pioneer in statistical graphics https://en.wikipedia.org/wiki/Florence_Nightingale#Statistics_and_sanitary_reform
                "nightingale",
                // Robert Noyce invented silicon integrated circuits and gave Silicon Valley its name. - https://en.wikipedia.org/wiki/Robert_Noyce
                "noyce",
                // Radia Perlman is a software designer and network engineer and most famous for her invention of the spanning-tree protocol (STP). https://en.wikipedia.org/wiki/Radia_Perlman
                "perlman",
                // Rob Pike was a key contributor to Unix, Plan 9, the X graphic system, utf-8, and the Go programming language. https://en.wikipedia.org/wiki/Rob_Pike
                "pike",
                // Laura Poitras is a director and producer whose work, made possible by open source crypto tools, advances the causes of truth and freedom of information by reporting disclosures by whistleblowers such as Edward Snowden. https://en.wikipedia.org/wiki/Laura_Poitras
                "poitras",
                // Dennis Ritchie - co-creator of UNIX and the C programming language. - https://en.wikipedia.org/wiki/Dennis_Ritchie
                "ritchie",
                // Ida Rhodes - American pioneer in computer programming, designed the first computer used for Social Security. https://en.wikipedia.org/wiki/Ida_Rhodes
                "rhodes",
                // Jean E. Sammet developed FORMAC, the first widely used computer language for symbolic manipulation of mathematical formulas. https://en.wikipedia.org/wiki/Jean_E._Sammet
                "sammet",
                // Mildred Sanderson - American mathematician best known for Sanderson's theorem concerning modular invariants. https://en.wikipedia.org/wiki/Mildred_Sanderson
                "sanderson",
                // Satoshi Nakamoto is the name used by the unknown person or group of people who developed bitcoin, authored the bitcoin white paper, and created and deployed bitcoin's original reference implementation. https://en.wikipedia.org/wiki/Satoshi_Nakamoto
                "satoshi",
                // Adi Shamir - Israeli cryptographer whose numerous inventions and contributions to cryptography include the Ferge Fiat Shamir identification scheme, the Rivest Shamir Adleman (RSA) public-key cryptosystem, the Shamir's secret sharing scheme, the breaking of the Merkle-Hellman cryptosystem, the TWINKLE and TWIRL factoring devices and the discovery of differential cryptanalysis (with Eli Biham). https://en.wikipedia.org/wiki/Adi_Shamir
                "shamir",
                // Betty Snyder - one of the original programmers of the ENIAC. https://en.wikipedia.org/wiki/ENIAC - https://en.wikipedia.org/wiki/Betty_Holberton
                "snyder",
                // Cynthia Solomon - Pioneer in the fields of artificial intelligence, computer science and educational computing. Known for creation of Logo, an educational programming language.  https://en.wikipedia.org/wiki/Cynthia_Solomon
                "solomon",
                // Frances Spence - one of the original programmers of the ENIAC. https://en.wikipedia.org/wiki/ENIAC - https://en.wikipedia.org/wiki/Frances_Spence
                "spence",
                // Michael Stonebraker is a database research pioneer and architect of Ingres, Postgres, VoltDB and SciDB. Winner of 2014 ACM Turing Award. https://en.wikipedia.org/wiki/Michael_Stonebraker
                "stonebraker",
                // Ivan Edward Sutherland - American computer scientist and Internet pioneer, widely regarded as the father of computer graphics. https://en.wikipedia.org/wiki/Ivan_Sutherland
                "sutherland",
                // Janese Swanson (with others) developed the first of the Carmen Sandiego games. She went on to found Girl Tech. https://en.wikipedia.org/wiki/Janese_Swanson
                "swanson",
                // Aaron Swartz was influential in creating RSS, Markdown, Creative Commons, Reddit, and much of the internet as we know it today. He was devoted to freedom of information on the web. https://en.wikiquote.org/wiki/Aaron_Swartz
                "swartz",
                // Nikola Tesla invented the AC electric system and every gadget ever used by a James Bond villain. https://en.wikipedia.org/wiki/Nikola_Tesla
                "tesla",
                // Ken Thompson - co-creator of UNIX and the C programming language - https://en.wikipedia.org/wiki/Ken_Thompson
                "thompson",
                // Linus Torvalds invented Linux and Git. https://en.wikipedia.org/wiki/Linus_Torvalds
                "torvalds",
                // Alan Turing was a founding father of computer science. https://en.wikipedia.org/wiki/Alan_Turing.
                "turing",
                // Dorothy Vaughan was a NASA mathematician and computer programmer on the SCOUT launch vehicle program that put America's first satellites into space - https://en.wikipedia.org/wiki/Dorothy_Vaughan
                "vaughan",
                // Marlyn Wescoff - one of the original programmers of the ENIAC. https://en.wikipedia.org/wiki/ENIAC - https://en.wikipedia.org/wiki/Marlyn_Meltzer
                "wescoff",
                // Sylvia B. Wilbur - British computer scientist who helped develop the ARPANET, was one of the first to exchange email in the UK and a leading researcher in computer-supported collaborative work. https://en.wikipedia.org/wiki/Sylvia_Wilbur
                "wilbur",
                // Malcolm John Williamson - British mathematician and cryptographer employed by the GCHQ. Developed in 1974 what is now known as Diffie-Hellman key exchange (Diffie and Hellman first published the scheme in 1976). https://en.wikipedia.org/wiki/Malcolm_J._Williamson
                "williamson",
                // Sophie Wilson designed the first Acorn Micro-Computer and the instruction set for ARM processors. https://en.wikipedia.org/wiki/Sophie_Wilson
                "wilson",
                // Jeannette Wing - co-developed the Liskov substitution principle. - https://en.wikipedia.org/wiki/Jeannette_Wing
                "wing",
                // Steve Wozniak invented the Apple I and Apple II. https://en.wikipedia.org/wiki/Steve_Wozniak
                "wozniak",

        };

    Random random = new Random();

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {

        boolean areThereToManyCreatedVoice = event.getGuild().getVoiceChannels().size() >= 10;

        if (areThereToManyCreatedVoice) {
            return;
        }

        String voiceName = LEFT[random.nextInt(LEFT.length)] + "_" + RIGHT[random.nextInt(RIGHT.length)];

        boolean isOnlyOneMemberInVoice = event.getChannelJoined().getMembers().size() == 1;

        if (isOnlyOneMemberInVoice) {

            Category category = event.getGuild().getCategoryById(779105998420967465L);
            event.getGuild().createVoiceChannel(voiceName, category).queue();
        }
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {

        boolean isVoiceLeftEmpty = event.getChannelLeft().getMembers().size() == 0;

        if (isVoiceLeftEmpty) {
            event.getChannelLeft().delete().queue();
        }
    }

    @Override
    public void onGuildVoiceMove(@NotNull GuildVoiceMoveEvent event) {

        boolean areThereToManyCreatedVoice = event.getGuild().getVoiceChannels().size() >= 10;

        if (areThereToManyCreatedVoice) {
            return;
        }

        String randomlyCombinedVoiceName = LEFT[random.nextInt(LEFT.length)] + "_" + RIGHT[random.nextInt(RIGHT.length)];
        Category voiceFunCategory = event.getGuild().getCategoryById(779105998420967465L);

        boolean isVoiceLeftEmpty = event.getChannelLeft().getMembers().size() == 0;
        boolean wasVoiceJoinedEmpty = event.getChannelJoined().getMembers().size() == 1;

        if (isVoiceLeftEmpty && wasVoiceJoinedEmpty) {
            event.getChannelLeft().delete().queue();
            event.getGuild().createVoiceChannel(randomlyCombinedVoiceName, voiceFunCategory).queue();
            return;
        }

        boolean wasVoiceJoinedNotEmpty = event.getChannelJoined().getMembers().size() >= 2;

        if (isVoiceLeftEmpty && wasVoiceJoinedNotEmpty) {
            event.getChannelLeft().delete().queue();
            return;

        }

        if (!isVoiceLeftEmpty && wasVoiceJoinedEmpty) {
            event.getGuild().createVoiceChannel(randomlyCombinedVoiceName, voiceFunCategory).queue();
        }
    }
}