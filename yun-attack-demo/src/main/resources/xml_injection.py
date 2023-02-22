from flask import Flask, request
from lxml import etree

app = Flask(__name__)


@app.route("/parse_xml", methods=["POST"])
def parse_xml():
    xml_data = request.data

    try:
        # 解析XML数据
        root = etree.fromstring(xml_data)
        result = etree.tostring(root, pretty_print=True).decode('utf-8')

        return result, 200
    except Exception as e:
        return str(e), 500


if __name__ == '__main__':
    app.run()