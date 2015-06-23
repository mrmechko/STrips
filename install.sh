wget https://github.com/mrmechko/flaming-tyrion/archive/v${data}.zip
ls
unzip -q ${data}
ls 
echo "strips.XMLSource = `pwd`/flaming-tyrion-${data}/lexicon/data/" > src/main/resources/application.conf
cat src/main/resources/application.conf
