# SopaDeLetras
adaptacion en gradle REST, del app cmd SopaDePalabrasen maven.
\n\n
Usa JPA/H2\n
1-POST completo\n
2-PUT, en desarrollo solo hasta validacion de coordenadas ingresadas\n
3-GET http://host/alphabetSoup/view/d041eaf2-0ac2-4376-812b-3e08be0bfd65, Endpoint para visualizar la sopa de letras, completo\n
4-GET http://host/alphabetSoup/list/d041eaf2-0ac2-4376-812b-3e08be0bfd65, Endpoint para visualizar la lista de palabras de una sopa, al 50%\n
\n\n
La app REST es en la ruta:
\n\n
Las app base para el desarrollo en:
\n\n\n\n\n

JSON importar en POSTMAN:
{
	"info": {
		"_postman_id": "cec8628a-de69-4339-9cd0-8d3da6d72c81",
		"name": "Sitrack",
		"schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json",
		"_exporter_id": "15706597"
	},
	"item": [
		{
			"name": "http://127.0.0.1:8088/alphabetSoup",
			"request": {
				"method": "GET",
				"header": [],
				"url": {
					"raw": "http://127.0.0.1:8088/sopaDeLetras/api/v1/sopaDeLetras",
					"protocol": "http",
					"host": [
						"127",
						"0",
						"0",
						"1"
					],
					"port": "8088",
					"path": [
						"sopaDeLetras",
						"api",
						"v1",
						"sopaDeLetras"
					]
				}
			},
			"response": []
		},
		{
			"name": "http://127.0.0.1:8088/alphabetSoup",
			"request": {
				"method": "POST",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": "{\r\n    \"descripcion\":\"campo descripcion\",\"fechaCreacion\":\"2021-05-09 09:15:45\",\"vigente\":true\r\n}",
					"options": {
						"raw": {
							"language": "json"
						}
					}
				},
				"url": {
					"raw": "http://127.0.0.1:8083/api/v1/tarea",
					"protocol": "http",
					"host": [
						"127",
						"0",
						"0",
						"1"
					],
					"port": "8083",
					"path": [
						"api",
						"v1",
						"tarea"
					]
				}
			},
			"response": []
		},
		{
			"name": "http://127.0.0.1:8088/alphabetSoup/977867f7-bba2-4b0c-9906-bf215fd30d60",
			"request": {
				"method": "GET",
				"header": [],
				"url": {
					"raw": "http://127.0.0.1:8088/api/v1/sopaDeLetras/2d28a371-ea4e-4fbb-be55-27727e81c561",
					"protocol": "http",
					"host": [
						"127",
						"0",
						"0",
						"1"
					],
					"port": "8088",
					"path": [
						"api",
						"v1",
						"sopaDeLetras",
						"2d28a371-ea4e-4fbb-be55-27727e81c561"
					]
				}
			},
			"response": []
		},
		{
			"name": "http://127.0.0.1:8088/alphabetSoup/list/",
			"request": {
				"method": "GET",
				"header": [],
				"url": {
					"raw": "http://127.0.0.1:8088/api/v1/sopaDeLetras/2d28a371-ea4e-4fbb-be55-27727e81c561",
					"protocol": "http",
					"host": [
						"127",
						"0",
						"0",
						"1"
					],
					"port": "8088",
					"path": [
						"api",
						"v1",
						"sopaDeLetras",
						"2d28a371-ea4e-4fbb-be55-27727e81c561"
					]
				}
			},
			"response": []
		},
		{
			"name": "http://127.0.0.1:8088/alphabetSoup/view/",
			"request": {
				"method": "GET",
				"header": [],
				"url": {
					"raw": "http://127.0.0.1:8088/api/v1/sopaDeLetras/2d28a371-ea4e-4fbb-be55-27727e81c561",
					"protocol": "http",
					"host": [
						"127",
						"0",
						"0",
						"1"
					],
					"port": "8088",
					"path": [
						"api",
						"v1",
						"sopaDeLetras",
						"2d28a371-ea4e-4fbb-be55-27727e81c561"
					]
				}
			},
			"response": []
		},
		{
			"name": "http://127.0.0.1:8088/alphabetSoup/",
			"request": {
				"method": "PUT",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": "{\r\n\t\"sr\":0,\r\n\t\"sc\":118,\r\n\t\"er\":0,\r\n\t\"ec\":19\r\n}\r\n",
					"options": {
						"raw": {
							"language": "json"
						}
					}
				},
				"url": {
					"raw": "http://127.0.0.1:8088/alphabetSoup/d0ea9ef6-29e8-42b2-85f4-98b2db7b3bbf",
					"protocol": "http",
					"host": [
						"127",
						"0",
						"0",
						"1"
					],
					"port": "8088",
					"path": [
						"alphabetSoup",
						"d0ea9ef6-29e8-42b2-85f4-98b2db7b3bbf"
					]
				}
			},
			"response": []
		}
	]
}


