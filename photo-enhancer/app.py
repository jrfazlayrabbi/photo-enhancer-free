echo from flask import Flask, render_template, request, redirect, url_for > app.py
echo from werkzeug.utils import secure_filename >> app.py
echo import os >> app.py
echo from PIL import Image, ImageEnhance >> app.py
echo. >> app.py
echo app = Flask(__name__) >> app.py
echo app.config['UPLOAD_FOLDER'] = 'uploads/' >> app.py
echo app.config['ALLOWED_EXTENSIONS'] = {'png', 'jpg', 'jpeg', 'gif'} >> app.py
echo. >> app.py
echo def allowed_file(filename): >> app.py
echo     return '.' in filename and filename.rsplit('.', 1)[1].lower() in app.config['ALLOWED_EXTENSIONS'] >> app.py
echo. >> app.py
echo def enhance_image(image_path): >> app.py
echo     image = Image.open(image_path) >> app.py
echo     enhancer = ImageEnhance.Sharpness(image) >> app.py
echo     enhanced_image = enhancer.enhance(2.0)  # Enhance sharpness by factor of 2 >> app.py
echo     enhanced_image_path = os.path.join(app.config['UPLOAD_FOLDER'], 'enhanced_' + os.path.basename(image_path)) >> app.py
echo     enhanced_image.save(enhanced_image_path) >> app.py
echo     return enhanced_image_path >> app.py
echo. >> app.py
echo @app.route('/', methods=['GET', 'POST']) >> app.py
echo def index(): >> app.py
echo     if request.method == 'POST': >> app.py
echo         if 'file' not in request.files: >> app.py
echo             return redirect(request.url) >> app.py
echo         file = request.files['file'] >> app.py
echo         if file.filename == '': >> app.py
echo             return redirect(request.url) >> app.py
echo         if file and allowed_file(file.filename): >> app.py
echo             filename = secure_filename(file.filename) >> app.py
echo             file_path = os.path.join(app.config['UPLOAD_FOLDER'], filename) >> app.py
echo             file.save(file_path) >> app.py
echo             enhanced_image_path = enhance_image(file_path) >> app.py
echo             return render_template('index.html', uploaded_image=filename, enhanced_image=enhanced_image_path) >> app.py
echo     return render_template('index.html') >> app.py
echo. >> app.py
echo if __name__ == '__main__': >> app.py
echo     app.run(debug=True) >> app.py
