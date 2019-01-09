import xlrd 

def excel_to_out(xroot, troot, sheet_num):
	file = xlrd.open_workbook(xroot)
	sheet = file.sheet_by_index(sheet_num)

	out = open(troot, 'w') 
	for rownum in range(sheet.nrows):
		#for st in sheet.row_values(rownum):
		for st in sheet.row_values(rownum):
			#print(st)
			out.write(str(st) + "\t")
		out.write("\n")
	out.close()

def main():
	print('beginning')
	excel_to_out("C:\\Users\\Sam Paniccia\\Desktop\\Dropbox\\DP Allele Planning.xlsx",
				 "C:\\w\\DP\\res\\gamedata\\allele_trait_vals.txt", 2)
	excel_to_out("C:\\Users\\Sam Paniccia\\Desktop\\Dropbox\\DP Allele Planning.xlsx",
				 "C:\\w\\DP\\res\\gamedata\\tile_trait_vals.txt", 3)
	excel_to_out("C:\\Users\\Sam Paniccia\\Desktop\\Dropbox\\DP Allele Planning.xlsx",
				 "C:\\w\\DP\\res\\gamedata\\org_tile_trait_vals.txt", 4)
	excel_to_out("C:\\Users\\Sam Paniccia\\Desktop\\Dropbox\\DP Allele Planning.xlsx",
				 "C:\\w\\DP\\res\\gamedata\\org_trait_vals.txt", 5)
	print('end')

main()