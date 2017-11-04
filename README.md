# mcf2pdf Converter for Mein CEWE Fotobuch (My CEWE Photobook) files to PDF

Latest Release: Version **0.3.3**

Author: Florian Albrecht

Downloads can be found [here](https://github.com/albrechtf/mcf2pdf/releases). Please read the **installation instructions** below carefully, **otherwise the software will definitely not work for you.** You have been warned.

## What is mcf2pdf? What not?

This program enables you to convert photobooks created with the Mein CEWE
Fotobuch or My CEWE Photobook software (in short, MCF software) to PDF files.
These files then can be used to have a quick and rough impression on how the 
photobook is going to look after printing.

**Calendars and anything else than BOOKS are NOT (yet) supported by this software!**
**(Do not ask me WHEN they will be supported - you will be ignored)**

The generated PDFs are NOT, and will never be, a 100% representation of the
designed photobook. Also, they do NOT include bleed margins (especially not for 
the cover pages) and such things, and they use the RGB color model, not the CMYK 
model used for professional printings. Therefore, you should not use the 
generated PDFs for any "real" print jobs - you will be really disappointed about 
the results.

I just developed this program for personal use. So, there will be many features 
of MCF files not yet supported by this software. This can result in empty or 
strange looking PDFs, or even in program crashes. Notice that this program is 
in BETA stage and is not guaranteed to work for ANY of your MCF files. However, 
if you notice any strange output, feel free to issue a ticket in the GitHub 
bugtracker. If possible, include a screenshot of the MCF software which shows how 
the page should look, and then a screenshot of the PDF how it looks after 
conversion. Also, including the whole .mcf file would help (don't panic, the .mcf 
files do not include any pictures by theirselves, but TEXT inserted into the 
photobook can be seen in the .mcf file).

If you are interested in extending the software, feel free to fork the project 
and start developing! Most important classes are commented. If you create Pull
Requests in the main project, I can review them and include them in the software.

## Installation and configuration

Installation is quite easy. Just download and extract the archive 
(mcf2pdf-x.y.z-bin-windows.zip or mcf2pdf-x.y.z-bin-linux.tar.gz).

To run mcf2pdf, you will have to **adjust the startup script by hand**, as the 
program cannot yet automatically determine where the MCF software is installed.

Some notes about the structure of the MCF software first: The installation 
locations of the software consist of two components. One component is the 
"real" installation directory, like `C:\Program Files\Mein CEWE Fotobuch` under 
Windows, or perhaps `/home/myuser/cewe-fotobuch` under Linux. Here the binaries
of the software are located as well as the shipped background images, fonts,
cliparts etc. 
The other component is the "temporary" directory of the MCF software which can
be set from within the software. Here everything you download from within the
program (e.g. new background images, cliparts, themes...) will be stored.

As files from both locations could be used in your photobook (.mcf) files, you
have to "tell" mcf2pdf about both. This is done by editing the startup script
(mcf2pdf.bat under Windows, mcf2pdf under Linux) with any text editor you like
(Windows: right-click the file and select "Edit"). 

Look for the line starting with &lt;SET> MCF_INSTALL_DIR= (SET only in the .bat 
version). After the =, insert the complete path to the "real" MCF software
installation location, e.g. "C:\Program Files\Mein CEWE Fotobuch". Notice that
you MUST include the path in double quotes if it contains spaces!!

Next, look for the line starting with &lt;SET> MCF_TEMP_DIR= (SET only in the 
.bat version). The same, insert now the TEMPORARY location of the software. If 
unsure, startup the MCF software, open any file, select "Options", "Directories"
(second item in the Options dialog), and copy the temporary directory listed 
there.
  
When finished, save the startup script.

## Converting files

After correct configuration (see above), conversion of MCF files now is rather
simple. Just open a command line (Windows: Win+R, then enter "cmd"), change to
the directory where you extracted the mcf2pdf program (if you do not know what
this means, google for "cd change directory"), and type "mcf2pdf &lt;my MCF file>
&lt;my new PDF file>". For example:

    mcf2pdf "C:\Documents and Settings\myuser\My Documents\USA 2011.mcf" output.pdf

Notice the double quotes due to the spaces in the path to the MCF file!

If this seems to complicated, you can COPY the .mcf file AND the images folder
of it (e.g. "USA 2011.mcf Files") to the directory where mcf2pdf is located, 
and then just enter

    mcf2pdf "USA 2011.mcf" output.pdf

The program will start converting the file and inform you when this is finished.
Notice that the program is NOT optimized for speed in any way... It can take 
a long time.

For advanced settings, enter "mcf2pdf -h". This will give you information on
how to adjust the output DPI setting and much more. 

## Troubleshooting

As stated above, mcf2pdf is BETA and does not yet implement very much of the
MCF software features. Please refer to chapter 1 on how to file a "bug".

If you get any error like "Unrecognized command: java", please make sure that
a Java Runtime (1.6.0 or newer) is installed and the Java Executable is on your
PATH. Ask Google if you do not know what this means.

If you get an error like "Java Heap Space" or OutOfMemoryException, 
there is not enough memory for the page rendering. Adjust the startup 
script (see chapter 2) to set higher memory  levels for Java. You can find memory 
settings in the line containing `MCF2PDF_JAVA_OPTS=...`. 
Increase the option `-Xmx128M` e.g. to 512M.

## Legal Stuff (Disclaimer)

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR 
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON 
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
