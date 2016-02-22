ADDRESS="pi5333.local"
USER="pi"

scp -r ./ $USER@$ADDRESS:/home/$USER/FRC2016/.
ssh $USER@$ADDRESS -C "(cd /home/$USER/FRC2016/Kinect && make); (cd /home/$USER/FRC2016/Splines/ && make)"
